package com.x8192Bit.DIYEdit_Mobile;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.hzc.widget.picker.file.FilePicker;
import com.hzc.widget.picker.file.FilePickerUiParams;
import com.xperia64.diyedit.FileByteOperations;
import com.xperia64.diyedit.metadata.GameMetadata;
import com.xperia64.diyedit.metadata.MangaMetadata;
import com.xperia64.diyedit.metadata.RecordMetadata;
import com.xperia64.diyedit.saveutils.SaveHandler;

import java.io.File;
import java.util.ArrayList;

import x8192Bit.DIYEdit_Mobile.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaveEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaveEditFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NAME = "name";
    private static final String ARG_MIOTYPE = "miotype";

    // TODO: Rename and change types of parameters
    private SaveHandler s;
    private String name;
    private int miotype;
    private int shelfNo = 1;
    private ArrayList<byte[]> mios;
    private SaveItemAdapter si;
    private GridView gv;
    private ImageButton diy1;
    private ImageButton diy2;
    private ImageButton diy3;
    private ImageButton diy4;
    private ImageButton diy5;


    // f. y. mem.

    public SaveEditFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gv = (GridView) getView().findViewById(R.id.ShelfGridView);

        diy1 = getView().findViewById(R.id.diy_1);
        diy2 = getView().findViewById(R.id.diy_2);
        diy3 = getView().findViewById(R.id.diy_3);
        diy4 = getView().findViewById(R.id.diy_4);
        diy5 = getView().findViewById(R.id.diy_5);
        int length = FileByteOperations.read(name).length;
        if (length == 4719808 || length == 591040 || length == 1033408/*||length == 6438592*/) {
            ((LinearLayout) getView().findViewById(R.id.ShelfItemsLayout)).removeView(diy5);
        } else {
            diy5.setOnClickListener(v -> onDIY5Pressed(v));
        }
        diy1.setOnClickListener(v -> onDIY1Pressed(v));
        diy2.setOnClickListener(v -> onDIY2Pressed(v));
        diy3.setOnClickListener(v -> onDIY3Pressed(v));
        diy4.setOnClickListener(v -> onDIY4Pressed(v));
        refreshShelf();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name name of the file.
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

    public void onDIY1Pressed(View v){
        shelfNo = 1;
        refreshShelf();
    }
    public void onDIY2Pressed(View v){
        shelfNo = 2;
        refreshShelf();
    }
    public void onDIY3Pressed(View v){
        shelfNo = 3;
        refreshShelf();
    }
    public void onDIY4Pressed(View v){
        shelfNo = 4;
        refreshShelf();
    }
    public void onDIY5Pressed(View v){
        shelfNo = 5;
        refreshShelf();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            miotype = getArguments().getInt(ARG_MIOTYPE);
        }
        s = new SaveHandler(name);
        si = new SaveItemAdapter();
    }

    public void refreshShelf() {
        GraphicsTools gt = new GraphicsTools();
        si.shelfItems.clear();
        si.titles.clear();
        if (miotype == 0) {
            for (int i = 18 * (shelfNo - 1); i < 18 * shelfNo; i++) {
                GraphicsTools.GameItem gi = gt.new GameItem();
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
                GraphicsTools.RecordItem gi = gt.new RecordItem();
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
                GraphicsTools.MangaItem gi = gt.new MangaItem();
                byte[] file = s.getMio(miotype, i);
                if (file != null) {
                    MangaMetadata mm = new MangaMetadata(file);
                    gi.setMangaColor(mm.getMangaColor());
                    gi.setIconColor(mm.getLogoColor());
                    gi.setIconShape(mm.getLogo());
                    si.titles.add(mm.getName());
                    si.shelfItems.add(i - 18 * (shelfNo - 1), gi);
                } else {
                    si.titles.add("");
                    si.shelfItems.add(i - 18 * (shelfNo - 1), null);
                }
            }
        }
        gv.setAdapter(si);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_saveedit, container, true);
    }

    private void openAssignFolder(String path) {
        File file = new File(path);
        if (file == null || !file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "file/*");
        try {
            startActivity(intent);
//            startActivity(Intent.createChooser(intent,"选择浏览工具"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showMioPopupMenu(View view, int count) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this.getContext(), view);

        // menu布局
        popupMenu.inflate(R.menu.miomenu);
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.importFile) {
                    //Intent i = new Intent(Intent.AC)
                    //s.setMio();
                    FilePicker.build(SaveEditFragment.this, 1)
                            .setOpenFile(new File("sdcard/"))
                            .setPickFileType(FilePickerUiParams.PickType.FILE)
                            .setSinglePick(new FilePicker.OnSinglePickListener() {
                                @Override
                                public void pick(@NonNull File path) {
                                    path.getAbsolutePath();
                                }

                                @Override
                                public void cancel() {
                                    Log.i("blahblahblah", "峨");
                                }
                            })
                            //打开选择界面
                            .open();
                    s.saveChanges();
                    refreshShelf();
                }
                if(item.getItemId() == R.id.extractFile) {
                    FilePicker.build(SaveEditFragment.this, 1)
                            .setOpenFile(new File("sdcard/"))
                            .setPickFileType(FilePickerUiParams.PickType.FOLDER)
                            .setSinglePick(new FilePicker.OnSinglePickListener() {
                                @Override
                                public void pick(@NonNull File path) {
                                    path.getAbsolutePath();
                                }

                                @Override
                                public void cancel() {
                                    Log.i("blahblahblah", "峨");
                                }
                            })
                            //打开选择界面
                            .open();
                    s.saveChanges();
                    refreshShelf();
                }
                if(item.getItemId() == R.id.deleteFile) {
                    s.deleteMio(miotype, count);
                    s.saveChanges();
                    refreshShelf();
                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Log.i("blahblahblah", "峨");
            }
        });

        popupMenu.show();
    }

    public class SaveItemAdapter extends BaseAdapter{
        public ArrayList<GraphicsTools.ShelfItem> shelfItems = new ArrayList<>(18);
        public ArrayList<String> titles = new ArrayList<>();
        private LayoutInflater layoutInflater;

        public SaveItemAdapter(){
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

        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return position%6;
        }

        /**
         * Get a View that displays the data at the specified position in the data set. You can either
         * create a View manually or inflate it from an XML layout file. When the View is inflated, the
         * parent View (GridView, ListView...) will apply default layout parameters unless you use
         * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
         * to specify a root view and to prevent attachment to the root.
         *
         * @param position    The position of the item within the adapter's data set of the item whose view
         *                    we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this view
         *                    is non-null and of an appropriate type before using. If it is not possible to convert
         *                    this view to display the correct data, this method can create a new view.
         *                    Heterogeneous lists can specify their number of view types, so that this View is
         *                    always of the right type (see {@link #getViewTypeCount()} and
         *                    {@link #getItemViewType(int)}).
         * @param parent      The parent that this view will eventually be attached to
         * @return A View corresponding to the data at the specified position.
         */
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
            GraphicsTools.ShelfItem si = shelfItems.get(position);
            if (si != null) {
                holder.iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                holder.iv.setImageDrawable(si.renderImage(getContext(), 128, 128));
                holder.tv.setText(titles.get(position));
                holder.iv.setOnClickListener(v -> showMioPopupMenu(v, position));
            } else {
                holder.tv.setText(titles.get(position));
                holder.iv.setOnClickListener(v -> showMioPopupMenu(v, position));
            }
            return convertView;
        }

        class ViewHolder{
            ImageView iv;
            TextView tv;
        }
    }
}