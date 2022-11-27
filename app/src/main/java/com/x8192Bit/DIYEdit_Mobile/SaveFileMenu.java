package com.x8192Bit.DIYEdit_Mobile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.tabs.TabItem;
import com.xperia64.diyedit.FileByteOperations;
import com.xperia64.diyedit.metadata.GameMetadata;

import x8192Bit.DIYEdit_Mobile.R;

public class SaveFileMenu extends AppCompatActivity {

    byte[] file;
    int savetype = 0;
    int miotype = 0;

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
            file = FileByteOperations.read(filePath);
            if (file.length == 33554432 || file.length == 33566720 || file.length == 33554554 || file.length == 4719808 || file.length == 6438592) {
                savetype |= 1;
                //GameMode.setEnabled(true);
                //DumpAll.setEnabled(true);
            }else if (file.length == 33554432 || file.length == 33566720 || file.length == 33554554 || file.length == 591040 || file.length == 6438592) {
                savetype |= 2;
                //RecordMode.setEnabled(true);
                //DumpAll.setEnabled(true);
            }else if (file.length == 33554432 || file.length == 33566720 || file.length == 33554554 || file.length == 1033408 || file.length == 6438592) {
                savetype |= 4;
                //MangaMode.setEnabled(true);
                //DumpAll.setEnabled(true);
            }else if (file.length == 33554432 || file.length == 33554554 || file.length == 33566720) {
                //UnlockMedals.setEnabled(true);
                //UnlockGames.setEnabled(true);
                //UnlockRecords.setEnabled(true);
                //UnlockManga.setEnabled(true);
            }else if(file.length!=65536)
            {
                //GameMetadata.setEnabled(false);
            }else if(file.length==8192)
            {
                miotype = 1;
                //tab1 = SaveFileMenu.this.getResources().getText(R.string.metaDataEditKey);
                //RecordMetadata.setEnabled(true);
                //PlayExportSong.setEnabled(true);
            }else if(file.length==14336)
            {
                miotype = 2;
                //MangaMetadata.setEnabled(true);
                //ViewManga.setEnabled(true);
            }else if(file.length==65536)
            {
                miotype = 3;
                //GameTest.setEnabled(true);
                //PlayGameBGM.setEnabled(true);
                //DumpAllObjects.setEnabled(true);
            }

            if(savetype != 0){
                setContentView(R.layout.activity_save_file_menu);
            }else if (miotype != 0){
                setContentView(R.layout.activity_mio_file_menu);
            }else{
                fileUnavailableAlert();
            }
            return 0;
        } else {
            fileUnavailableAlert();
            return -1;
        }
    }

    private void fileUnavailableAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SaveFileMenu.this);
        alertDialogBuilder.setTitle(R.string.warningKey).setMessage(R.string.couldNotReadFileKey).setCancelable(false);
        alertDialogBuilder.setNeutralButton(R.string.backKey,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SaveFileMenu.this.finish();
            }
        });
        alertDialogBuilder.show();
    }
}