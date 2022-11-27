package com.x8192Bit.DIYEdit_Mobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import x8192Bit.DIYEdit_Mobile.R;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.x8192Bit.DIYEdit_Mobile.MESSAGE";

    // Create Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static final int CHOOSE_FILE_CODE = 0;


    // Call System File Manager
    public void chooseFile(View v) {

        //使用兼容库就无需判断系统版本
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
            //拥有权限，执行操作
            fileChooser();
        }else{
            //没有权限，向用户请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, R.string.permissonNeedKey);
        }
    }

    private void fileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*").addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Choose File"), CHOOSE_FILE_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.noFMKey, Toast.LENGTH_SHORT).show();
        }
    }

    private void openFile(String realPath){
        Intent intent = new Intent(this, SaveFileMenu.class);
        intent.putExtra(EXTRA_MESSAGE, realPath);
        startActivity(intent);
    }
    // System call on File Manager Done
    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_FILE_CODE) {
                openFile(FileTools.getFileAbsolutePath(MainActivity.this, data.getData()));
            }
        } else {
            Log.e(null, "onActivityResult() error, resultCode: " + resultCode);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    public void openRecentFile(View view){
        // TODO : Read data from
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        String[] listdemo = {"0","1","2","3","4","5","6","7","8","9"};
        alertDialogBuilder.setTitle(R.string.openRecentFileKey).setItems(listdemo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openFile(null);
            }
        });
    }

    public void settingsMenu(View view){
        // TODO : Jump to another Activity
    }
    public void exit(View view){
        android.os.Process.killProcess(android.os.Process.myPid());    //获取PID
        System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
    }
}