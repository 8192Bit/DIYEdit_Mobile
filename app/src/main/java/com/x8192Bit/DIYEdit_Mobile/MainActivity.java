package com.x8192Bit.DIYEdit_Mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hzc.widget.picker.file.FilePicker;
import com.hzc.widget.picker.file.FilePickerUiParams;

import java.io.File;

import x8192Bit.DIYEdit_Mobile.R;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.x8192Bit.DIYEdit_Mobile.MESSAGE";

    private String filePath;

    // Create Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    // Called when the first button is pressed
    public void chooseFile(View v) {
        FilePicker.build(MainActivity.this, 1)
                .setOpenFile(new File("sdcard/"))
                .setPickFileType(FilePickerUiParams.PickType.FILE)
                .setSinglePick(new FilePicker.OnSinglePickListener() {
                    @Override
                    public void pick(@NonNull File path) {
                        filePath = path.getAbsolutePath();
                    }

                    @Override
                    public void cancel() {
                        Log.i("blahblahblah", "峨");
                    }
                })
                //打开选择界面
                .open();
    }


    // Called when the second button is pressed
    public void openRecentFile(View view){
        SharedPreferences sp = this.getSharedPreferences("SP",MODE_PRIVATE);
        String history = sp.getString("history",null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        if(history == null){
            alertDialogBuilder.setTitle(R.string.warningKey).setMessage(R.string.noHistoryKey).show();
        }else {
            String[] paths = history.split(",");
            String[] items = new String[paths.length];
            for (int i = 0; i < items.length; i++) {
                String[] slashSplitted = paths[i].split("/");
                items[i] = slashSplitted[slashSplitted.length - 1];
            }
            alertDialogBuilder.setTitle(R.string.openRecentFileKey).setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    openFile(paths[which]);
                }
            }).show();
        }
    }

    // Called when the third button is pressed
    public void settingsMenu(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    // Called when the fourth button is pressed
    public void exit(View view){
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    // Common method for open a file
    private void openFile(String realPath){
        Intent intent = new Intent(this, SaveFileMenu.class);
        intent.putExtra(EXTRA_MESSAGE, realPath);
        startActivity(intent);
    }


}