package com.x8192Bit.DIYEdit_Mobile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.tabs.TabItem;
import com.xperia64.diyedit.metadata.GameMetadata;

import x8192Bit.DIYEdit_Mobile.R;

public class SaveFileMenu extends AppCompatActivity {

    byte[] file;
    int savetype = 0;

    TabItem tab1 = null;
    TabItem tab2 = null;
    TabItem tab3 = null;
    TabItem tab4 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mio_file_menu);
        TabItem tab1 = findViewById(R.id.tab1);
        TabItem tab2 = findViewById(R.id.tab2);
        TabItem tab3 = findViewById(R.id.tab3);
        TabItem tab4 = findViewById(R.id.tab4);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String path = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        //initEnable(path);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                //process here
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param filePath Provide the filepath which got by the Sys FM.
     * @return if 0
     */
    public int initEnable(String filePath) {
        if (filePath != null){
            if (file.length == 33554432 || file.length == 33566720 || file.length == 33554554 || file.length == 4719808 || file.length == 6438592) {
                savetype |= 1;
                //GameMode.setEnabled(true);
                //DumpAll.setEnabled(true);
            }
            if (file.length == 33554432 || file.length == 33566720 || file.length == 33554554 || file.length == 591040 || file.length == 6438592) {
                savetype |= 2;
                //RecordMode.setEnabled(true);
                //DumpAll.setEnabled(true);
            }
            if (file.length == 33554432 || file.length == 33566720 || file.length == 33554554 || file.length == 1033408 || file.length == 6438592) {
                savetype |= 4;
                //MangaMode.setEnabled(true);
                //DumpAll.setEnabled(true);
            }
            if (file.length == 33554432 || file.length == 33554554 || file.length == 33566720) {
                //UnlockMedals.setEnabled(true);
                //UnlockGames.setEnabled(true);
                //UnlockRecords.setEnabled(true);
                //UnlockManga.setEnabled(true);
            }

            if(file.length!=65536)
            {
                //GameMetadata.setEnabled(false);
            }
            if(file.length==8192)
            {
                //RecordMetadata.setEnabled(true);
                //PlayExportSong.setEnabled(true);
            }
            if(file.length==14336)
            {
                //MangaMetadata.setEnabled(true);
                //ViewManga.setEnabled(true);
            }
            if(file.length==65536)
            {
                //GameTest.setEnabled(true);
                //PlayGameBGM.setEnabled(true);
                //DumpAllObjects.setEnabled(true);
            }

            return 0;
        } else {
            return -1;
        }
    }

}