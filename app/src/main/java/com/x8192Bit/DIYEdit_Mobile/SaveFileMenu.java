package com.x8192Bit.DIYEdit_Mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xperia64.diyedit.FileByteOperations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import x8192Bit.DIYEdit_Mobile.R;

public class SaveFileMenu extends AppCompatActivity {

    byte[] file;
    int savetype = 0;
    int miotype = 0;
    TabLayout tabs = null;
    TextView SSaveLabel = null;
    TextView MNameLabel = null;
    TextView MAuthorLabel = null;
    TextView MCompanyLabel = null;
    TextView MTimeLabel = null;
    ViewPager2 vp2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String path = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        initEnable(path);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish(); // back button
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param filePath Provide the filepath which got by the Sys FM.
     */
    @SuppressLint("SetTextI18n")// well at least, GDATA, MDATA and RDATA IS I18N
    public void initEnable(String filePath) {
        if (filePath != null){
            file = FileByteOperations.read(filePath);
            ArrayList<Fragment> fl = new ArrayList<>();
            if (file.length == 4719808) {
                //GDATA
                savetype |= 1;
                setMenuType(contentViewType.SAVE);
                TextView saveView = findViewById(R.id.saveView);
                saveView.setText(getText(R.string.wiiSaveKey) + " (GDATA)");
                TabLayout.Tab t0 = tabs.newTab();
                t0.setText(R.string.editGameKey);
                tabs.addTab(t0,0);
                fl.add(SaveEditFragment.newInstance(filePath,0));
            }else if (file.length == 591040) {
                //RDATA
                savetype |= 2;
                setMenuType(contentViewType.SAVE);
                TextView saveView = findViewById(R.id.saveView);
                saveView.setText(getText(R.string.wiiSaveKey) + " (RDATA)");
                TabLayout.Tab t0 = tabs.newTab();
                t0.setText(R.string.editRecordKey);
                tabs.addTab(t0,0);
                fl.add(SaveEditFragment.newInstance(filePath,1));
            }else if (file.length == 1033408) {
                //MDATA
                savetype |= 4;
                setMenuType(contentViewType.SAVE);
                TextView saveView = findViewById(R.id.saveView);
                saveView.setText(getText(R.string.wiiSaveKey) + " (MDATA)");
                TabLayout.Tab t0 = tabs.newTab();
                t0.setText(R.string.editMangaKey);
                tabs.addTab(t0,0);
                fl.add(SaveEditFragment.newInstance(filePath,2));
            }else if (file.length == 6438592) {
                //maybe wii all-in-one save file?
                //don't know
                savetype |= 7;
                setMenuType(contentViewType.SAVE);
                setContentView(R.layout.activity_save_file_menu);
                TextView saveView = findViewById(R.id.saveView);
                saveView.setText(R.string.wiiSaveKey);
                tabs = findViewById(R.id.tabs);
                TabLayout.Tab t0 = tabs.newTab();
                TabLayout.Tab t1 = tabs.newTab();
                TabLayout.Tab t2 = tabs.newTab();
                t0.setText(R.string.editGameKey);
                t1.setText(R.string.editRecordKey);
                t2.setText(R.string.editMangaKey);
                tabs.addTab(t0,0);
                tabs.addTab(t1,1);
                tabs.addTab(t2,2);
                fl.add(SaveEditFragment.newInstance(filePath,0));
                fl.add(SaveEditFragment.newInstance(filePath,1));
                fl.add(SaveEditFragment.newInstance(filePath,2));
            } else if (file.length == 33554432 || file.length == 33554554 || file.length == 33566720) {
                savetype |= 8;
                setMenuType(contentViewType.SAVE);
                TextView saveView = findViewById(R.id.saveView);
                saveView.setText(R.string.dsSaveKey);
                tabs = findViewById(R.id.tabs);
                TabLayout.Tab t0 = tabs.newTab();
                TabLayout.Tab t1 = tabs.newTab();
                TabLayout.Tab t2 = tabs.newTab();
                TabLayout.Tab t3 = tabs.newTab();
                t0.setText(R.string.editGameKey);
                t1.setText(R.string.editRecordKey);
                t2.setText(R.string.editMangaKey);
                t3.setText(R.string.unlockKey);
                tabs.addTab(t0,0);
                tabs.addTab(t1,1);
                tabs.addTab(t2,2);
                tabs.addTab(t3,3);
                fl.add(SaveEditFragment.newInstance(filePath,0));
                fl.add(SaveEditFragment.newInstance(filePath,1));
                fl.add(SaveEditFragment.newInstance(filePath,2));
                fl.add(UnlockFragment.newInstance(filePath));
            }
            // Only detect when the file is not a save file
            if(savetype == 0) {
                if (file.length == 8192) {
                    miotype = 1;
                    setMenuType(contentViewType.MIO);
                    TabLayout.Tab t0 = tabs.newTab();
                    TabLayout.Tab t1 = tabs.newTab();
                    t0.setText(R.string.metaDataEditKey);
                    t1.setText(R.string.midiToolsKey);
                    tabs.addTab(t0);
                    tabs.addTab(t1);
                } else if (file.length == 14336) {
                    miotype = 2;
                    setMenuType(contentViewType.MIO);
                    TabLayout.Tab t0 = tabs.newTab();
                    TabLayout.Tab t1 = tabs.newTab();
                    t0.setText(R.string.metaDataEditKey);
                    t1.setText(R.string.viewMangaKey);
                    tabs.addTab(t0, 0);
                    tabs.addTab(t1, 1);
                } else if (file.length == 65536) {
                    miotype = 3;
                    setMenuType(contentViewType.MIO);
                    TabLayout.Tab t0 = tabs.newTab();
                    TabLayout.Tab t1 = tabs.newTab();
                    TabLayout.Tab t2 = tabs.newTab();
                    TabLayout.Tab t3 = tabs.newTab();
                    t0.setText(R.string.metaDataEditKey);
                    t1.setText(R.string.viewBGKey);
                    t2.setText(R.string.midiToolsKey);
                    t3.setText(R.string.exportAllOBJKey);
                    tabs.addTab(t0, 0);
                    tabs.addTab(t1, 1);
                    tabs.addTab(t2, 2);
                    tabs.addTab(t3, 3);
                }
            }
            vp2 = findViewById(R.id.saveViewPager);
            vp2.setAdapter(new FragmentStateAdapter(this) {
                @NonNull
                @Override
                public Fragment createFragment(int position) {
                    return fl.get(position);
                }

                @Override
                public int getItemCount() {
                    return fl.size();
                }
            });
            new TabLayoutMediator(tabs, vp2, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    tab.setText(tab.getText());
                }
            }).attach();
            if(savetype != 0){
                updateDetection(filePath,fileType.SAVE);
            }else if (miotype != 0){
                updateDetection(filePath,fileType.MIO);
                MTimeLabel = findViewById(R.id.timeValue);

            }else{
                updateDetection(filePath,fileType.UNREADABLE);
                fileUnavailableAlert();
            }
        } else {
            fileUnavailableAlert();
        }
    }

    void setMenuType(contentViewType tvv){
        if (tvv == contentViewType.MIO && miotype != 0){
            setContentView(R.layout.activity_mio_file_menu);
        }
        if (tvv == contentViewType.SAVE && savetype != 0){
            setContentView(R.layout.activity_save_file_menu);
        }
        if(tabs == null) {
            tabs = findViewById(R.id.tabs);
        }
    }

    enum fileType{
        MIO,
        SAVE,
        UNREADABLE
    }

    enum contentViewType{
        MIO,
        SAVE
    }

    private void updateFileHistory(@NonNull SharedPreferences sp, String realPath){
        int historyCount =sp.getInt("historyCount",10);
        SharedPreferences.Editor ed = sp.edit();
        String history = sp.getString("history","0");
        ArrayList<String> historyArray = new ArrayList<String>(Arrays.asList(history.split(",")));
        historyArray.add(0,realPath);
        if(historyArray.size()>historyCount){
            for(int i = historyCount; i<historyArray.size(); i++){
                historyArray.remove(i);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < historyArray.size(); i++) {
            sb.append(historyArray.get(i)).append(",");
        }
        ed.putString("history",sb.toString());
        ed.apply();
    }

    private void updateDetection(String realPath, fileType ft) {
        SharedPreferences sp = SaveFileMenu.this.getSharedPreferences("SP",MODE_PRIVATE);
        int typeToUpdate = sp.getInt("typeToUpdate",7);
        if(ft == fileType.MIO){
            if ((typeToUpdate | 4) == typeToUpdate) {
                //mio
                updateFileHistory(sp,realPath);
            }
        }else if(ft == fileType.SAVE){
            if ((typeToUpdate | 2) == typeToUpdate) {
                //save
                updateFileHistory(sp,realPath);
            }
        }else if ((typeToUpdate | 1) == typeToUpdate && ft == fileType.UNREADABLE) {
            //unreadable
            updateFileHistory(sp,realPath);
        }
        //     MSU
        //00000111
        //     Mio
        //      Save
        //       Unreadable
    }

    private void fileUnavailableAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SaveFileMenu.this);
        alertDialogBuilder.setTitle(R.string.warningKey).setMessage(R.string.couldNotReadFileKey).setCancelable(false);
        alertDialogBuilder.setNeutralButton(R.string.backKey, (dialog, which) -> SaveFileMenu.this.finish());
        alertDialogBuilder.show();
    }
}