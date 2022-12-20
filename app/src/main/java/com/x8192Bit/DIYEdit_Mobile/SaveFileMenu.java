package com.x8192Bit.DIYEdit_Mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.xperia64.diyedit.FileByteOperations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String path = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        tabs = findViewById(R.id.tabs);
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

    private void addTab(TabLayout.Tab t, TabLayout tl){
        tl.addTab(getAddNewTab(t));
    }

    private TabLayout.Tab getAddNewTab(@NonNull TabLayout.Tab tab) {
        try {
            Field mParent = TabLayout.Tab.class.getDeclaredField("mParent");
            mParent.setAccessible(true);
            Object o = mParent.get(tab);
            if (o != this) { //检测tab的mParent是不是this。
                return getAddNewTab(tabs.newTab());//如果不是从新获取并重新检查。
            } else {
                return tab;//如果是直接返回。
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed creating Tab", e);
        }
    }

    /**
     *
     * @param filePath Provide the filepath which got by the Sys FM.
     */
    @SuppressLint("MissingInflatedId")
    public void initEnable(String filePath) {
        if (filePath != null){
            file = FileByteOperations.read(filePath);
            // 待会儿再来处理这坨意面屎罢
            // 能跑就行
            // 额
            // TODO: deal with these Italian codes
            int positionIndex = 0;
            if (file.length == 33554432 || file.length == 33566720 || file.length == 33554554 || file.length == 4719808 || file.length == 6438592) {
                savetype |= 1;
                setMenuType(contentViewType.SAVE);
                tabs = findViewById(R.id.tabs);
                TabLayout.Tab t0 = tabs.newTab();
                t0.setText(R.string.editGameKey);
                tabs.addTab(t0,positionIndex++);
            }
            if (file.length == 33554432 || file.length == 33566720 || file.length == 33554554 || file.length == 591040 || file.length == 6438592) {
                savetype |= 2;
                setMenuType(contentViewType.SAVE);
                tabs = findViewById(R.id.tabs);
                TabLayout.Tab t0 = tabs.newTab();
                t0.setText(R.string.editRecordKey);
                tabs.addTab(t0,positionIndex++);
            }
            if (file.length == 33554432 || file.length == 33566720 || file.length == 33554554 || file.length == 1033408 || file.length == 6438592) {
                savetype |= 4;
                setMenuType(contentViewType.SAVE);
                tabs = findViewById(R.id.tabs);
                TabLayout.Tab t0 = tabs.newTab();
                t0.setText(R.string.editMangaKey);
                tabs.addTab(t0,positionIndex++);
            }
            if (file.length == 33554432 || file.length == 33554554 || file.length == 33566720) {
                setMenuType(contentViewType.SAVE);
                tabs = findViewById(R.id.tabs);
                TabLayout.Tab t0 = tabs.newTab();
                t0.setText(R.string.unlockKey);
                tabs.addTab(t0,positionIndex++);
            }
            if(file.length==8192)
            {
                miotype = 1;
                setMenuType(contentViewType.MIO);
                tabs = findViewById(R.id.tabs);
                TabLayout.Tab t0 = tabs.newTab();
                TabLayout.Tab t1 = tabs.newTab();
                t0.setText(R.string.metaDataEditKey);
                t1.setText(R.string.midiToolsKey);
                tabs.addTab(t0);
                tabs.addTab(t1);
            }else if(file.length==14336)
            {
                miotype = 2;
                setMenuType(contentViewType.MIO);
                tabs = findViewById(R.id.tabs);
                TabLayout.Tab t0 = tabs.newTab();
                TabLayout.Tab t1 = tabs.newTab();
                t0.setText(R.string.metaDataEditKey);
                t1.setText(R.string.viewMangaKey);
                tabs.addTab(t0,0);
                tabs.addTab(t1,1);
            }else if(file.length==65536)
            {
                miotype = 3;
                setMenuType(contentViewType.MIO);
                tabs = findViewById(R.id.tabs);
                TabLayout.Tab t0 = tabs.newTab();
                TabLayout.Tab t1 = tabs.newTab();
                TabLayout.Tab t2 = tabs.newTab();
                TabLayout.Tab t3 = tabs.newTab();
                t0.setText(R.string.metaDataEditKey);
                t1.setText(R.string.viewBGKey);
                t2.setText(R.string.midiToolsKey);
                t3.setText(R.string.exportAllOBJKey);
                tabs.addTab(t0,0);
                tabs.addTab(t1,1);
                tabs.addTab(t2,2);
                tabs.addTab(t3,3);
            }
            if(savetype != 0){
                updateDetection(filePath,fileType.MIO);
                MTimeLabel = findViewById(R.id.timeValue);
            }else if (miotype != 0){
                updateDetection(filePath,fileType.SAVE);
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

    private void updateFileHistory(SharedPreferences sp, String realPath){
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