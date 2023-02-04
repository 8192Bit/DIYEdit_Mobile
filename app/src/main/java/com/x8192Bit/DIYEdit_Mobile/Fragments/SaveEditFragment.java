package com.x8192Bit.DIYEdit_Mobile.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.codekidlabs.storagechooser.StorageChooser;
import com.x8192Bit.DIYEdit_Mobile.Utils.GraphicsUtils;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaveEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaveEditFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_MIOTYPE = "miotype";

    private String name;
    private int miotype;
    private int shelfNo = 1;
    private SaveItemAdapter si;
    private GridView gv;

    public SaveEditFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name    name of the file.
     * @param miotype mio type.
     * @return A new instance of fragment GameEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SaveEditFragment newInstance(String name, int miotype) {
        SaveEditFragment fragment = new SaveEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putInt(ARG_MIOTYPE, miotype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gv = (GridView) getView().findViewById(R.id.ShelfGridView);

        ImageButton diy1 = getView().findViewById(R.id.diy_1);
        ImageButton diy2 = getView().findViewById(R.id.diy_2);
        ImageButton diy3 = getView().findViewById(R.id.diy_3);
        ImageButton diy4 = getView().findViewById(R.id.diy_4);
        ImageButton diy5 = getView().findViewById(R.id.diy_5);
        int length = FileByteOperations.read(name).length;
        if (length == 4719808 || length == 591040 || length == 1033408 || length == 6438592) {
            ((LinearLayout) getView().findViewById(R.id.ShelfItemsLayout)).removeView(diy5);
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
            miotype = getArguments().getInt(ARG_MIOTYPE);
        }
        si = new SaveItemAdapter();
    }

    public void refreshShelf() {
        SaveHandler s = new SaveHandler(name);
        GraphicsUtils gt = new GraphicsUtils();
        si.shelfItems.clear();
        si.titles.clear();
        if (miotype == 0) {
            for (int i = 18 * (shelfNo - 1); i < 18 * shelfNo; i++) {
                GraphicsUtils.GameItem gi = gt.new GameItem();
                byte[] file = s.getMio(miotype, i);
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
        if (miotype == 1) {
            for (int i = 18 * (shelfNo - 1); i < 18 * shelfNo; i++) {
                GraphicsUtils.RecordItem gi = gt.new RecordItem();
                byte[] file = s.getMio(miotype, i);
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
        if (miotype == 2) {
            for (int i = 18 * (shelfNo - 1); i < 18 * shelfNo; i++) {
                GraphicsUtils.MangaItem gi = gt.new MangaItem();
                byte[] file = s.getMio(miotype, i);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saveedit, container, true);
    }

    @Deprecated
    private void showMioPopupMenu(View view, int count) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        // menu布局
        popupMenu.inflate(R.menu.miomenu);
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(item -> {
            SaveHandler s = new SaveHandler(name);
            if (item.getItemId() == R.id.importFile) {
                StorageChooser chooser = new StorageChooser.Builder()
                        .withActivity(getActivity())
                        .withFragmentManager(getActivity().getFragmentManager())
                        .withMemoryBar(true)
                        .allowCustomPath(true)
                        .setType(StorageChooser.FILE_PICKER)
                        .build();
                chooser.show();
                chooser.setOnSelectListener(pathImport -> {
                    int length = FileByteOperations.read(pathImport).length;
                    int size;
                    switch (miotype) {
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
                            throw new IllegalStateException("Unexpected value: " + miotype);
                    } // switch mio file length should be
                    if (length == size) {
                        s.setMio(pathImport, count + 18 * (shelfNo - 1));
                        s.saveChanges();
                        refreshShelf();
                    } else {
                        Toast.makeText(getContext(), R.string.couldNotReadFileKey, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (item.getItemId() == R.id.extractFile) {
                StorageChooser chooser = new StorageChooser.Builder()
                        .withActivity(getActivity())
                        .withFragmentManager(getActivity().getFragmentManager())
                        .withMemoryBar(true)
                        .allowCustomPath(true)
                        .setType(StorageChooser.DIRECTORY_CHOOSER)
                        .build();
                chooser.show();
                chooser.setOnSelectListener(pathExtract -> {
                    EditText fileNameEdit = new EditText(getContext());
                    AlertDialog ad = new AlertDialog.Builder(getContext())
                            .setTitle("Set export file name")
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
                                    Toast.makeText(getContext(), "NOOOOOOOOOOO!!!!", Toast.LENGTH_SHORT).show();
                                }
                                FileByteOperations.write(s.getMio(miotype, count + 18 * (shelfNo - 1)), pathName);
                                s.saveChanges();
                                refreshShelf();
                            })
                            .create();
                    ad.setOnShowListener(dialog -> ad
                            .getButton(AlertDialog.BUTTON_NEUTRAL)
                            .setOnClickListener(v -> {

                                String key = "";
                                switch (miotype) {
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
                                Metadata m = new Metadata(s.getMio(miotype, count + 18 * (shelfNo - 1)));

                                DateTime date = new DateTime(2000, 1, 1, 0, 0, 0, 0);
                                date = date.plusDays(m.getTimestamp());
                                String name = String.format("%s-%s(%s)-%04d (%s) (%04d-%02d-%02d) %s.mio",
                                        key, m.getCreator(), m.getBrand(), m.getSerial2(), (m.getRegion()) ? "J" : "UE", date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), (m.getLocked() ? "(#) " : "") + m.getName());
                                name = name.replace('?', '¿').replace('/', '〳').replace('\\', '〳').replace(':', '：').replace('*', '★').replace('\"', '\'').replace('<', '＜').replace('>', '＞').replace('|', 'l').replace('!', '¡');
                                fileNameEdit.setText(name);
                            }));
                    ad.show();
                });

            }
            if (item.getItemId() == R.id.deleteFile) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.warningKey)
                        .setMessage("你正在删除一个mio文件！请确认。")
                        .setCancelable(false)
                        .setPositiveButton(R.string.okKey, (dialog, which) -> {
                            s.deleteMio(miotype, count + 18 * (shelfNo - 1));
                            s.saveChanges();
                            refreshShelf();
                        })
                        .setNegativeButton(R.string.backKey, null)
                        .show();
            }
            return false;
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(menu -> Log.i("blahblahblah", "峨"));

        popupMenu.show();
    }


    public class SaveItemAdapter extends BaseAdapter {
        private final LayoutInflater layoutInflater;
        public ArrayList<GraphicsUtils.ShelfItem> shelfItems = new ArrayList<>(18);
        public ArrayList<String> titles = new ArrayList<>();

        public SaveItemAdapter() {
            layoutInflater = LayoutInflater.from(getActivity());
            // Needs to be invoked after parent activity started
        }

        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            return shelfItems.size();
        }

        /**
         * Get the data item associated with the specified position in the data set.
         *
         * @param position Position of the item whose data we want within the adapter's
         *                 data set.
         * @return The data at the specified position.
         */
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
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.shelf_item_layout, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.ShelfItemImageView);
                holder.tv = (TextView) convertView.findViewById(R.id.ShelfItemTextView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            GraphicsUtils.ShelfItem si = shelfItems.get(position);
            if (si != null) {
                holder.iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                holder.iv.setImageBitmap(si.renderImage(getContext(), 256, 256));
                holder.tv.setText(titles.get(position));
                holder.iv.setOnClickListener(v -> showMioPopupMenu(v, position));
            } else {
                holder.tv.setText("");
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