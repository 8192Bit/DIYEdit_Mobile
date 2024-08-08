package com.x8192bit.diyeditmobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.x8192bit.diyeditmobile.utils.FileUtils;
import com.x8192bit.diyeditmobile.utils.SPUtils;

import x8192Bit.DIYEdit_Mobile.BuildConfig;
import x8192Bit.DIYEdit_Mobile.R;

public class MainActivity extends BaseActivity {

    public static final String REAL_PATH = "com.x8192bit.diyeditmobile.REAL_PATH";
    public static final String CHOOSE_TYPE = "com.x8192bit.diyeditmobile.CHOOSE_TYPE";
    public static final String CHOOSE_RESULT = "com.x8192bit.diyeditmobile.CHOOSE_RESULT";

    public static void setUIMode(String UIMode) {
        switch (UIMode) {
            case "system":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case "day":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "night":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getData() != null) {
            Uri uri = intent.getData();
            openFile(FileUtils.getFileAbsolutePath(this, uri));
        }
    }

    // Create Method
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main_activity);

            // set UI mode
            setUIMode(SPUtils.getTheme(getApplicationContext()));

            // open file if opening a mio/save file from other app
            if (getIntent().getData() != null) {
                Uri uri = getIntent().getData();
                openFile(FileUtils.getFileAbsolutePath(this, uri));
            }

            // set version label
            ((TextView) findViewById(R.id.main_version_tv)).setText(getText(R.string.app_name) +
                    " " + BuildConfig.VERSION_NAME +
                    " " + BuildConfig.BUILD_TYPE.toUpperCase());

            // set click event
            findViewById(R.id.main_open_file_btn).setOnClickListener(this::selectFile);
            findViewById(R.id.main_recent_file_btn).setOnClickListener(this::openRecentFile);
            findViewById(R.id.main_settings_btn).setOnClickListener(this::openSettingsMenu);
            findViewById(R.id.main_exit_btn).setOnClickListener(this::exit);

        } catch (Exception e) {
            new AlertDialog.Builder(getApplicationContext())
                    .setMessage(e.getMessage())
                    .show();
        }
    }

    @Deprecated
    public void selectFile(View v) {
        try {
            if (Build.VERSION.SDK_INT < 30) {
                if (!checkBefore30()) {
                    requestBefore30();
                } else {
                    // User granted file permission, Access your file
                    readFiles();
                }
            } else {
                check30AndAfter();
            }
        } catch (Exception e) {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage(e.getMessage())
                    .show();
        }
    }

    private boolean checkBefore30() {
        return ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestBefore30() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, R.string.storagePermissionRequireKey, Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFiles();
            } else {
                // Allow permission for storage access!
                Toast.makeText(MainActivity.this, R.string.tryAgainKey, Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Deprecated
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
        } else {
            readFiles();
        }
    }

    private void readFiles() {
        Intent intent = new Intent(this, FileSelectActivity.class);
        intent.putExtra(CHOOSE_TYPE, FileSelectActivity.CHOOSE_FILE);
        // TODO 快点重构你这坨狗屎
        startActivityForResult(intent, 114514);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 114514 && resultCode == 1919810) {
            if (data != null) {
                openFile(data.getStringExtra(CHOOSE_RESULT));
            } else {
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("嘿嘿！！")
                        .setMessage("安卓崩喽！安卓崩喽！！！1111")
                        .show();
            }
        }
    }

    // LOOSEN COUPLING BUT LOW PERFORMANCE!!!!11
    public void openRecentFile(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        String[] HistoryItems = SPUtils.getHistoryItems(getApplicationContext());
        String[] HistoryPaths = SPUtils.getHistoryPaths(getApplicationContext());
        if (HistoryPaths == null) {
            alertDialogBuilder
                    .setTitle(R.string.warningKey)
                    .setMessage(R.string.noHistoryKey)
                    .setNegativeButton(R.string.okKey, (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            alertDialogBuilder
                    .setTitle(R.string.openRecentFileKey)
                    .setItems(HistoryItems, (dialog, which) -> openFile(HistoryPaths[which]))
                    .show();
        }
    }

    public void openSettingsMenu(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void exit(View view) {
        this.finish();
    }

    // Common method for open a file
    private void openFile(String realPath) {
        Intent intent = new Intent(this, SaveEditActivity.class);
        intent.putExtra(REAL_PATH, realPath);
        startActivity(intent);
    }

}