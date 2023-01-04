package com.x8192Bit.DIYEdit_Mobile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xperia64.diyedit.metadata.GameMetadata;
import com.xperia64.diyedit.saveutils.SaveHandler;

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
    private ImageButton diy1;
    private ImageButton diy2;
    private ImageButton diy3;
    private ImageButton diy4;
    private ImageButton diy5;


    // f. y. mem.

    public SaveEditFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
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

    public void onDIY1Pressed(){
        shelfNo = 1;
        refreshShelf();
    }
    public void onDIY2Pressed(){
        shelfNo = 2;
        refreshShelf();
    }
    public void onDIY3Pressed(){
        shelfNo = 3;
        refreshShelf();
    }
    public void onDIY4Pressed(){
        shelfNo = 4;
        refreshShelf();
    }
    public void onDIY5Pressed(){
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
        refreshShelf();
    }

    public void refreshShelf(){
        GraphicsTools gt = new GraphicsTools();
        for(int i = 18*(shelfNo-1); i<18*shelfNo; i++){
            GraphicsTools.GameItem gi = gt.new GameItem();
            GameMetadata gm = new GameMetadata(s.getMio(miotype, i));
            gi.setCartridgeColor(gm.getCartColor());
            gi.setCartridgeShape(gm.getCartType());
            gi.setIconColor(gm.getLogoColor());
            gi.setIconShape(gm.getLogo());
            si.shelfItems.set(i-(18*shelfNo-1),gi);
        }
        GridView gv = (GridView) getView().findViewById(R.id.ShelfGridView);
        gv.setAdapter(si);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
/*
        diy1 = getView().findViewById(R.id.diy_1);
        diy2 = getView().findViewById(R.id.diy_2);
        diy3 = getView().findViewById(R.id.diy_3);
        diy4 = getView().findViewById(R.id.diy_4);
        diy5 = getView().findViewById(R.id.diy_5);
        diy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDIY1Pressed();
            }
        });
        diy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDIY2Pressed();
            }
        });
        diy3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDIY3Pressed();
            }
        });
        diy4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDIY4Pressed();
            }
        });
        diy5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDIY5Pressed();
            }
        });
        */


        return inflater.inflate(R.layout.fragment_saveedit, container, true);
    }

    private void showMioPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this.getContext(), view);

        // menu布局
        popupMenu.inflate(R.menu.miomenu);
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.importFile) {
                }
                if(item.getItemId() == R.id.extractFile) {
                }
                if(item.getItemId() == R.id.deleteFile) {
                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });

        popupMenu.show();
    }

    public class SaveItemAdapter extends BaseAdapter{
        public ArrayList<GraphicsTools.ShelfItem> shelfItems = new ArrayList<>();
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
            return position/6;
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
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            GraphicsTools.ShelfItem si = shelfItems.get(position);
            if (si != null) {
                holder.iv.setImageDrawable(si.renderImage(getContext(),60,60));
            }
            return convertView;
        }

        class ViewHolder{
            ImageView iv;
            TextView tv;
        }
    }

}