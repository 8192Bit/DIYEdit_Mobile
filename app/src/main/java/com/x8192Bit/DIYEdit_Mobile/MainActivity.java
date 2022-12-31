package com.x8192Bit.DIYEdit_Mobile;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
        ImageView iv = findViewById(R.id.imageViewIcon);

        GraphicsTools gt = new GraphicsTools();
        GraphicsTools.GameIcon gi = gt.new GameIcon();
        gi.setCartridgeColor(gt.LIGHTBLUE);
        gi.setCartridgeShape(gt.GG_GBA);
        gi.setIconColor(gt.GREEN);
        gi.setIconShape(gt.GI_PULSE);
        iv.setImageDrawable(gi.renderImage(this,iv.getWidth(),iv.getHeight()));
    }

    private static final int CHOOSE_FILE_CODE = 0;


    // Called when the first button is pressed
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

    // Called when the second button is pressed
    public void openRecentFile(View view){
        SharedPreferences sp = this.getSharedPreferences("SP",MODE_PRIVATE);
        String history = sp.getString("history",null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        if(history == null){
            alertDialogBuilder.setTitle(R.string.warningKey).setMessage(R.string.noHistoryKey).show();
        }else {
            String[] items = history.split(",");
            alertDialogBuilder.setTitle(R.string.openRecentFileKey).setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    openFile(items[which].toString());
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

    // Open sys fileman
    @SuppressWarnings("deprecation")// 我偏要用startActivityForResult
    private void fileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*").addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Choose File"), CHOOSE_FILE_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.noFMKey, Toast.LENGTH_SHORT).show();
        }
    }

    // Common method for open a file
    private void openFile(String realPath){
        Intent intent = new Intent(this, SaveFileMenu.class);
        intent.putExtra(EXTRA_MESSAGE, realPath);
        startActivity(intent);
    }

    @Override
    // System call on fileman Done
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

}