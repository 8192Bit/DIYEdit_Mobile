package com.x8192Bit.DIYEdit_Mobile;

import static com.x8192Bit.DIYEdit_Mobile.MainActivity.CHOOSE_RESULT;
import static com.x8192Bit.DIYEdit_Mobile.fragments.SaveEditFragment.IS_IMPORT_MIO;
import static com.x8192Bit.DIYEdit_Mobile.fragments.SaveEditFragment.IS_SAVE_EDIT;
import static com.x8192Bit.DIYEdit_Mobile.fragments.SaveEditFragment.SAVE_EDIT_COUNT;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import x8192Bit.DIYEdit_Mobile.R;


public class FileChooseActivity extends AppCompatActivity {

    public static final int CHOOSE_FILE = 0;
    public static final int CHOOSE_DIRECTORY = 1;
    File CurrentPath = null;
    private int chooseType = CHOOSE_FILE;
    private boolean isImportMIO;
    private boolean isSaveEdit;
    private int saveEditCount;
    private boolean isTimeSorted;

    //TODO UPDATE!!!!!!!!!!!!!!!!!!!!!!!!FUK LISTVIEW
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_choose);

        Intent intent = getIntent();
        chooseType = intent.getIntExtra(MainActivity.CHOOSE_TYPE, CHOOSE_FILE);

        Button directoryButton = findViewById(R.id.fileChooseButton);

        if (chooseType == CHOOSE_FILE) {
            ((LinearLayout) findViewById(R.id.fileChooseLayout)).removeView(directoryButton);
        } else {
            directoryButton.setOnClickListener(v -> {
                try {
                    Intent intentResult = new Intent();
                    String path = CurrentPath.getAbsolutePath();
                    intentResult.putExtra(CHOOSE_RESULT, path);
                    intentResult.putExtra(IS_SAVE_EDIT, isSaveEdit);
                    if (isSaveEdit) {
                        intentResult.putExtra(IS_IMPORT_MIO, isImportMIO);
                        intentResult.putExtra(SAVE_EDIT_COUNT, saveEditCount);
                    }
                    setResult(1919810, intentResult);
                    finish();
                } catch (Exception e) {
                    new AlertDialog.Builder(FileChooseActivity.this)
                            .setTitle("What the fuck????!!")
                            .setMessage("GO FUCK YOURSELF!!!!!!!!!!!!!!!!!11").show();
                }
            });
        }
        isSaveEdit = intent.getBooleanExtra(IS_SAVE_EDIT, false);
        if (isSaveEdit) {
            isImportMIO = intent.getBooleanExtra(IS_IMPORT_MIO, false);
            saveEditCount = intent.getIntExtra(SAVE_EDIT_COUNT, 0);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        CurrentPath = Environment.getExternalStorageDirectory();

        refreshList(getApplicationContext());

        ListView fileChooser = findViewById(R.id.fileChooseView);
        SharedPreferences sp = this.getSharedPreferences("com.x8192Bit.DIYEdit_Mobile_preferences", MODE_PRIVATE);

        isTimeSorted = sp.getBoolean("isTimeOrdered", false);


        fileChooser.setOnItemClickListener((adapterView, view, i, l) -> {
            File originalPath = CurrentPath;
            try {
                String path = (String) (((TextView) ((LinearLayout) view).getChildAt(1))).getText();
                if (path.equals("..")) {
                    CurrentPath = CurrentPath.getParentFile();
                } else {
                    CurrentPath = new File(CurrentPath.getAbsolutePath() + '/' + path);
                    if (CurrentPath.isFile() && chooseType == CHOOSE_FILE) {
                        Intent intentResult = new Intent();
                        intentResult.putExtra(CHOOSE_RESULT, CurrentPath.getAbsolutePath());
                        intentResult.putExtra(IS_SAVE_EDIT, isSaveEdit);
                        if (isSaveEdit) {
                            intentResult.putExtra(IS_IMPORT_MIO, isImportMIO);
                            intentResult.putExtra(SAVE_EDIT_COUNT, saveEditCount);
                        }
                        setResult(1919810, intentResult);
                        finish();
                    }
                }
                refreshList(getApplicationContext());
            } catch (NullPointerException e) {
                CurrentPath = originalPath;
            } catch (Exception e) {
                new AlertDialog.Builder(FileChooseActivity.this)
                        .setTitle("What the fuck????!!")
                        .setMessage("GO FUCK YOURSELF!!!!!!!!!!!!!!!!!11").show();
                CurrentPath = originalPath;
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int sortType = isTimeSorted ? R.string.timeSortededKey : R.string.nameSortededKey;
        menu.add(0, 0, 0, sortType);
        menu.add(0, 0, 0, R.string.reverseOrderKey);
        return super.onCreateOptionsMenu(menu);
    }

    public void refreshList(Context c) {
        ListView fileChooser = findViewById(R.id.fileChooseView);
        TextView PathView = findViewById(R.id.fileChooseTextView);
        String[] from = {"img", "text"};
        int[] to = {R.id.FileChooseItemImageView, R.id.FileChooseItemTextView};
        ArrayList<Map<String, Object>> files = new ArrayList<>();
        HashMap<String, Object> parentFolder = new HashMap<>();
        parentFolder.put("img", R.drawable.exit);
        parentFolder.put("text", "..");
        files.add(parentFolder);
        for (File f : Objects.requireNonNull(CurrentPath.listFiles())) {
            HashMap<String, Object> file = new HashMap<>();
            if (f.isFile()) {
                String splited[] = f.getName().split("\\.");
                if (f.getName().equals("MDATA") || f.getName().equals("GDATA") || f.getName().equals("RDATA")) {
                    file.put("img", R.drawable.save_wii);
                } else if (splited.length > 1 && (splited[splited.length - 1].equals("sav") || splited[splited.length - 1].equals("dsv"))) {
                    file.put("img", R.drawable.save_ds);
                } else if (splited.length > 1 && splited[splited.length - 1].equals("mio")) {
                    file.put("img", R.mipmap.ic_launcher_foreground);
                } else {
                    file.put("img", R.drawable.baseline_file_open_24);
                }
            } else if (f.isDirectory()) {
                file.put("img", R.drawable.directory);
            }
            file.put("text", f.getName());
            files.add(file);
        }
        fileChooser.setAdapter(new SimpleAdapter(c, files, R.layout.filechoose_item_layout, from, to));
        PathView.setText(CurrentPath.getAbsolutePath());
    }
}