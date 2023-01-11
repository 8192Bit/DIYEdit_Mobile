package com.x8192Bit.DIYEdit_Mobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.codekidlabs.storagechooser.StorageChooser;

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
        if (Build.VERSION.SDK_INT < 30) {
            if (!checkBefore30()) {
                Toast.makeText(MainActivity.this, "NO PERMISSION", Toast.LENGTH_LONG).show();
                requestBefore30();
            } else {
                // User granted file permission, Access your file

                readFiles();
            }
        } else {
            check30AndAfter();
        }
    }

    private boolean checkBefore30() {
        return ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    private void requestBefore30() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Storage permission required. Please allow this permission", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission for storage access successful!
                // Read your files now
                Toast.makeText(MainActivity.this, "YATTAZE!!!!!!!!", Toast.LENGTH_LONG).show();

                readFiles();
            } else {
                // Allow permission for storage access!
                Toast.makeText(MainActivity.this, "Please try again.", Toast.LENGTH_LONG).show();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private void check30AndAfter() {
        if (!Environment.isExternalStorageManager()) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, 200);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 200);
            }
        }
    }

    private void readFiles() {
        StorageChooser chooser = new StorageChooser.Builder()
                .withActivity(MainActivity.this)
                .withFragmentManager(getFragmentManager())
                .withMemoryBar(true)
                .allowCustomPath(true)
                .setType(StorageChooser.FILE_PICKER)
                .build();

        chooser.show();
        chooser.setOnSelectListener(this::openFile);

    }


    // Called when the second button is pressed
    public void openRecentFile(View view) {
        SharedPreferences sp = this.getSharedPreferences("SP", MODE_PRIVATE);
        String history = sp.getString("history", null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        if (history == null) {
            alertDialogBuilder.setTitle(R.string.warningKey).setMessage(R.string.noHistoryKey).show();
        } else {
            String[] paths = history.split(",");
            String[] items = new String[paths.length];
            for (int i = 0; i < items.length; i++) {
                String[] slashSplited = paths[i].split("/");
                items[i] = slashSplited[slashSplited.length - 1];
            }
            alertDialogBuilder.setTitle(R.string.openRecentFileKey).setItems(items, (dialog, which) -> openFile(paths[which])).show();
        }
    }

    // Called when the third button is pressed
    public void settingsMenu(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    // Called when the fourth button is pressed
    public void exit(View view) {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    // Common method for open a file
    private void openFile(String realPath) {
        Intent intent = new Intent(this, SaveFileMenu.class);
        intent.putExtra(EXTRA_MESSAGE, realPath);
        startActivity(intent);
    }


}