package com.x8192Bit.DIYEdit_Mobile.fragments;

import static com.x8192Bit.DIYEdit_Mobile.SaveFileMenu.FILE_CHOOSE_ACTIVITY_RESULT;
import static com.x8192Bit.DIYEdit_Mobile.SaveFileMenu.FILE_CHOOSE_REAL_PATH;
import static com.x8192Bit.DIYEdit_Mobile.SaveFileMenu.OPEN_FILE_CHOOSE_ACTIVITY;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.x8192Bit.DIYEdit_Mobile.utils.GraphicsUtils;
import com.xperia64.diyedit.FileByteOperations;
import com.xperia64.diyedit.metadata.GameMetadata;
import com.xperia64.diyedit.metadata.MangaMetadata;
import com.xperia64.diyedit.metadata.Metadata;
import com.xperia64.diyedit.metadata.RecordMetadata;
import com.xperia64.diyedit.saveutils.SaveHandler;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import x8192Bit.DIYEdit_Mobile.R;

public class SaveEditFragment extends Fragment {

    private static final String ARG_NAME = "name";
    public static final String IS_IMPORT_MIO = "com.x8192Bit.DIYEdit_Mobile.IS_IMPORT_MIO";
    public static final String IS_SAVE_EDIT = "com.x8192Bit.DIYEdit_Mobile.IS_SAVE_EDIT";
    public static final String SAVE_EDIT_COUNT = "com.x8192Bit.DIYEdit_Mobile.SAVE_EDIT_COUNT";
    private static final String ARG_MIO_TYPE = "mio_type";
    private String name;
    private int mio_type;
    private int shelfNo = 1;
    private SaveItemAdapter si;
    private GridView gv;

    public SaveEditFragment() {
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SaveHandler s = new SaveHandler(name);
            if (intent.getBooleanExtra(IS_IMPORT_MIO, true)) {
                String pathImport = intent.getStringExtra(FILE_CHOOSE_REAL_PATH);
                int count = intent.getIntExtra(SAVE_EDIT_COUNT, 0);
                int length = FileByteOperations.read(pathImport).length;
                int size;
                switch (mio_type) {
                    case 0:
                        size = 65536;
                        break;
                    case 1:
                        size = 8192;
                        break;
                    case 2:
                        size = 14336;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + mio_type);
                }
                if (length == size) {
                    s.setMio(pathImport, count + 18 * (shelfNo - 1));
                    s.saveChanges();
                    refreshShelf();
                } else {
                    Toast.makeText(getContext(), R.string.couldNotReadFileKey, Toast.LENGTH_SHORT).show();
                }
            } else {

                String pathExtract = intent.getStringExtra(FILE_CHOOSE_REAL_PATH);
                int count = intent.getIntExtra(SAVE_EDIT_COUNT, 0);
                EditText fileNameEdit = new EditText(getContext());
                AlertDialog ad = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.exportFileNameSetKey)
                        .setCancelable(true)
                        .setView(fileNameEdit)
                        .setNeutralButton("默认", null)
                        .setPositiveButton(R.string.okKey, (dialog, which) -> {
                            String fileName = fileNameEdit.getText().toString();
                            if (!fileName.toLowerCase(Locale.US).endsWith(".mio")) {
                                fileName += ".mio";
                            }
                            String pathName = pathExtract + "//" + fileName;
                            try {
                                new File(pathName).createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), R.string.warningKey, Toast.LENGTH_SHORT).show();
                            }
                            FileByteOperations.write(s.getMio(mio_type, count + 18 * (shelfNo - 1)), pathName);
                            s.saveChanges();
                            refreshShelf();
                        })
                        .create();
                ad.setOnShowListener(dialog -> ad
                        .getButton(AlertDialog.BUTTON_NEUTRAL)
                        .setOnClickListener(v -> {

                            String key = "";
                            switch (mio_type) {
                                case 0:
                                    key = "G";
                                    break;
                                case 1:
                                    key = "R";
                                    break;
                                case 2:
                                    key = "M";
                                    break;
                            }
                            Metadata m = new Metadata(s.getMio(mio_type, count + 18 * (shelfNo - 1)));

                            DateTime date = new DateTime(2000, 1, 1, 0, 0, 0, 0);
                            date = date.plusDays(m.getTimestamp());
                            @SuppressLint("DefaultLocale") String name = String.format("%s-%s(%s)-%04d (%s) (%04d-%02d-%02d) %s.mio",
                                    key, m.getCreator(), m.getBrand(), m.getSerial2(), (m.getRegion()) ? "J" : "UE", date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), (m.getLocked() ? "(#) " : "") + m.getName());
                            name = name.replace('?', '¿')
                                    .replace('/', '〳')
                                    .replace('\\', '〳')
                                    .replace(':', '：')
                                    .replace('*', '★')
                                    .replace('\"', '\'')
                                    .replace('<', '＜')
                                    .replace('>', '＞')
                                    .replace('|', 'l')
                                    .replace('!', '¡');
                            fileNameEdit.setText(name);
                        }));
                ad.show();

            }
        }
    };

    public static SaveEditFragment newInstance(String name, int mio_type) {
        SaveEditFragment fragment = new SaveEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putInt(ARG_MIO_TYPE, mio_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gv = requireView().findViewById(R.id.ShelfGridView);

        MaterialButton diy1 = requireView().findViewById(R.id.diy_1);
        MaterialButton diy2 = requireView().findViewById(R.id.diy_2);
        MaterialButton diy3 = requireView().findViewById(R.id.diy_3);
        MaterialButton diy4 = requireView().findViewById(R.id.diy_4);
        MaterialButton diy5 = requireView().findViewById(R.id.diy_5);
        int length = FileByteOperations.read(name).length;
        if (length == 4719808 || length == 591040 || length == 1033408 || length == 6438592) {
            ((LinearLayout) requireView().findViewById(R.id.ShelfItemsLayout)).removeView(diy5);
        } else {
            diy5.setOnClickListener(v -> {
                shelfNo = 5;
                refreshShelf();
            });
        }
        diy1.setOnClickListener(v -> {
            shelfNo = 1;
            refreshShelf();
        });
        diy2.setOnClickListener(v -> {
            shelfNo = 2;
            refreshShelf();
        });
        diy3.setOnClickListener(v -> {
            shelfNo = 3;
            refreshShelf();
        });
        diy4.setOnClickListener(v -> {
            shelfNo = 4;
            refreshShelf();
        });
        refreshShelf();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            mio_type = getArguments().getInt(ARG_MIO_TYPE);
        }
        si = new SaveItemAdapter();
        IntentFilter intentFilter = new IntentFilter(FILE_CHOOSE_ACTIVITY_RESULT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            requireActivity().registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saveedit, container, true);
    }

    public void refreshShelf() {
        SaveHandler s = new SaveHandler(name);
        GraphicsUtils gt = new GraphicsUtils();
        si.shelfItems.clear();
        si.titles.clear();
        if (mio_type == 0) {
            for (int i = 18 * (shelfNo - 1); i < 18 * shelfNo; i++) {
                GraphicsUtils.GameItem gi = gt.new GameItem();
                byte[] file = s.getMio(mio_type, i);
                if (file != null) {
                    GameMetadata gm = new GameMetadata(file);
                    gi.setCartridgeColor(gm.getCartColor());
                    gi.setCartridgeShape(gm.getCartType());
                    gi.setIconColor(gm.getLogoColor());
                    gi.setIconShape(gm.getLogo());
                    si.titles.add(gm.getName());
                    si.shelfItems.add(i - 18 * (shelfNo - 1), gi);
                } else {
                    si.titles.add("");
                    si.shelfItems.add(i - 18 * (shelfNo - 1), null);
                }
            }
        }
        if (mio_type == 1) {
            for (int i = 18 * (shelfNo - 1); i < 18 * shelfNo; i++) {
                GraphicsUtils.RecordItem gi = gt.new RecordItem();
                byte[] file = s.getMio(mio_type, i);
                if (file != null) {
                    RecordMetadata rm = new RecordMetadata(file);
                    gi.setRecordColor(rm.getRecordColor());
                    gi.setRecordShape(rm.getRecordType());
                    gi.setIconColor(rm.getLogoColor());
                    gi.setIconShape(rm.getLogo());
                    si.titles.add(rm.getName());
                    si.shelfItems.add(i - 18 * (shelfNo - 1), gi);
                } else {
                    si.titles.add("");
                    si.shelfItems.add(i - 18 * (shelfNo - 1), null);
                }
            }
        }
        if (mio_type == 2) {
            for (int i = 18 * (shelfNo - 1); i < 18 * shelfNo; i++) {
                GraphicsUtils.MangaItem gi = gt.new MangaItem();
                byte[] file = s.getMio(mio_type, i);
                if (file != null) {
                    MangaMetadata mm = new MangaMetadata(file);
                    gi.setMangaColor(mm.getMangaColor());
                    gi.setIconColor(mm.getLogoColor());
                    gi.setIconShape(mm.getLogo());
                    si.titles.add(mm.getName());
                    si.shelfItems.add(i - 18 * (shelfNo - 1), gi);
                } else {
                    si.titles.add(null);
                    si.shelfItems.add(i - 18 * (shelfNo - 1), null);
                }
            }
        }
        gv.setAdapter(si);
    }

    @Deprecated
    private void showMioPopupMenu(View view, int count) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.inflate(R.menu.miomenu);
        popupMenu.setOnMenuItemClickListener(item -> {
            SaveHandler s = new SaveHandler(name);
            if (item.getItemId() == R.id.importFile) {
                Intent intent = new Intent(OPEN_FILE_CHOOSE_ACTIVITY);
                intent.putExtra(IS_SAVE_EDIT, true);
                intent.putExtra(IS_IMPORT_MIO, true);
                intent.putExtra(SAVE_EDIT_COUNT, count);
                requireActivity().sendBroadcast(intent);
            }
            if (item.getItemId() == R.id.extractFile) {
                Intent intent = new Intent(OPEN_FILE_CHOOSE_ACTIVITY);
                intent.putExtra(IS_SAVE_EDIT, true);
                intent.putExtra(IS_IMPORT_MIO, false);
                intent.putExtra(SAVE_EDIT_COUNT, count);
                requireActivity().sendBroadcast(intent);
            }
            if (item.getItemId() == R.id.deleteFile) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.warningKey)
                        .setMessage(R.string.deleteMIOWaringKey)
                        .setCancelable(false)
                        .setPositiveButton(R.string.okKey, (dialog, which) -> {
                            s.deleteMio(mio_type, count + 18 * (shelfNo - 1));
                            s.saveChanges();
                            refreshShelf();
                        })
                        .setNegativeButton(R.string.backKey, null)
                        .show();
            }
            return false;
        });
        popupMenu.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(broadcastReceiver);
    }

    public class SaveItemAdapter extends BaseAdapter {
        private final LayoutInflater layoutInflater;
        public ArrayList<GraphicsUtils.ShelfItem> shelfItems = new ArrayList<>(18);
        public ArrayList<String> titles = new ArrayList<>();

        public SaveItemAdapter() {
            layoutInflater = LayoutInflater.from(getActivity());
            // Needs to be invoked after parent activity started
        }

        @Override
        public int getCount() {
            return shelfItems.size();
        }

        @Override
        public Object getItem(int position) {
            return shelfItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position % 6;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.shelf_item_layout, null);
                holder = new ViewHolder();
                holder.iv = convertView.findViewById(R.id.ShelfItemImageView);
                holder.tv = convertView.findViewById(R.id.ShelfItemTextView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            GraphicsUtils.ShelfItem si = shelfItems.get(position);
            holder.tv.setSelected(true);
            if (si != null) {
                holder.iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                holder.iv.setImageDrawable(si.renderImage(getContext()));
                holder.tv.setText(titles.get(position));
                holder.iv.setOnClickListener(v -> showMioPopupMenu(v, position));
            } else {
                holder.tv.setText(R.string.nullSlotKey);
                holder.iv.setOnClickListener(v -> showMioPopupMenu(v, position));
            }
            return convertView;
        }

        class ViewHolder {
            ImageView iv;
            TextView tv;
        }
    }
}