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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleEventObserver;

import com.codekidlabs.storagechooser.StorageChooser;
import com.x8192Bit.DIYEdit_Mobile.utils.FileUtils;

import java.util.concurrent.atomic.AtomicInteger;

import x8192Bit.DIYEdit_Mobile.BuildConfig;
import x8192Bit.DIYEdit_Mobile.R;

public class MainActivity extends AppCompatActivity {

    public static final String REAL_PATH = "com.x8192Bit.DIYEdit_Mobile.REAL_PATH";
    public static final String CHOOSE_TYPE = "com.x8192Bit.DIYEdit_Mobile.CHOOSE_TYPE";
    public static final String CHOOSE_RESULT = "com.x8192Bit.DIYEdit_Mobile.CHOOSE_RESULT";

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
            setContentView(R.layout.activity_main);

            ((TextView) findViewById(R.id.MainVersionTextView)).setText(getText(R.string.app_name) + " " + BuildConfig.VERSION_NAME + ' ' + BuildConfig.BUILD_TYPE.toUpperCase());

            findViewById(R.id.OpenFileButton).setOnClickListener(this::chooseFile);
            findViewById(R.id.RecentFileButton).setOnClickListener(this::openRecentFile);
            findViewById(R.id.SettingsButton).setOnClickListener(this::settingsMenu);
            findViewById(R.id.ExitButton).setOnClickListener(this::exit);

            if (getIntent().getData() != null) {
                Uri uri = getIntent().getData();
                openFile(FileUtils.getFileAbsolutePath(this, uri));
            }
        } catch (Exception e) {
            new AlertDialog.Builder(getApplicationContext())
                    .setMessage(e.getMessage())
                    .show();
        }
    }

    public void chooseFile(View v) {
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
            Toast.makeText(MainActivity.this, "Storage permission required. Please allow this permission", Toast.LENGTH_LONG).show();
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
                Toast.makeText(MainActivity.this, "Please try again.", Toast.LENGTH_LONG).show();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Deprecated// 爱用不用吧 反正新版API多恶心大家都知道
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

    private void readFiles(){

        Intent intent = new Intent(this, FileChooseActivity.class);
        intent.putExtra(CHOOSE_TYPE, FileChooseActivity.CHOOSE_FILE);
        ActivityResultContracts.StartActivityForResult contract = new ActivityResultContracts.StartActivityForResult();
        ActivityResultCallback<ActivityResult> callback = o -> {
        };
        startActivityForResult(intent,114514);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==114514 && resultCode==1919810) {
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

    public void openRecentFile(View view) {
        SharedPreferences sp = this.getSharedPreferences("com.x8192Bit.DIYEdit_Mobile_preferences", MODE_PRIVATE);
        String history = sp.getString("history", null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        if (history == null) {
            alertDialogBuilder.setTitle(R.string.warningKey).setMessage(R.string.noHistoryKey).show();
        } else {
            String[] paths = history.split(",");
            String[] items = new String[paths.length];
            if (sp.getBoolean("showFullPath", false)) {
                items = paths.clone();
            } else {
                for (int i = 0; i < items.length; i++) {
                    String[] slashSplited = paths[i].split("/");
                    items[i] = slashSplited[slashSplited.length - 1];
                }
            }
            alertDialogBuilder.setTitle(R.string.openRecentFileKey).setItems(items, (dialog, which) -> openFile(paths[which])).show();
        }
    }

    public void settingsMenu(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void exit(View view) {
        try{
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);}catch(Exception e){


        }
    }

    // Common method for open a file
    private void openFile(String realPath) {
        Intent intent = new Intent(this, SaveFileMenu.class);
        intent.putExtra(REAL_PATH, realPath);
        startActivity(intent);
    }



}