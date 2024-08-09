package com.x8192bit.diyeditmobile;

import static com.x8192bit.diyeditmobile.MainActivity.CHOOSE_RESULT;
import static com.x8192bit.diyeditmobile.fragments.SaveEditFragment.IS_IMPORT_MIO;
import static com.x8192bit.diyeditmobile.fragments.SaveEditFragment.IS_SAVE_EDIT;
import static com.x8192bit.diyeditmobile.fragments.SaveEditFragment.SAVE_EDIT_COUNT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import com.x8192bit.diyeditmobile.utils.SPUtils;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import x8192bit.diyeditmobile.R;


public class FileSelectActivity extends BaseActivity {
    // TODO 快点重构你这坨狗屎
    public static final int CHOOSE_FILE = 0;
    public static final int CHOOSE_DIRECTORY = 1;
    File CurrentPath = null;
    private int chooseType = CHOOSE_FILE;
    private boolean isImportMIO;
    private boolean isSaveEdit;
    private int saveEditCount;
    private boolean isTimeSorted;
    private boolean isNormalOrder;
    private boolean showHiddenFiles;
    public AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                new AlertDialog.Builder(FileSelectActivity.this)
                        .setTitle("What the fuck????!!")
                        .setMessage("GO FUCK YOURSELF!!!!!!!!!!!!!!!!!11").show();
                CurrentPath = originalPath;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileselect_activity);

        Intent intent = getIntent();
        chooseType = intent.getIntExtra(MainActivity.CHOOSE_TYPE, CHOOSE_FILE);

        Button selectDirectoryButton = findViewById(R.id.file_select_ok_btn);

        if (chooseType == CHOOSE_FILE) {
            // When selecting a file, the OK button is useless so remove
            ((LinearLayout) findViewById(R.id.file_select_ll)).removeView(selectDirectoryButton);
        } else {
            selectDirectoryButton.setOnClickListener(v -> {
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
                    new AlertDialog.Builder(FileSelectActivity.this)
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

        // initialize the actionbar so that we can add some button to it then
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_activity_file_choose);
        }

        CurrentPath = Environment.getExternalStorageDirectory();

        ListView fileChooser = findViewById(R.id.file_select_lv);

        isTimeSorted = SPUtils.isTimeSorted(getApplicationContext());
        isNormalOrder = SPUtils.isNormalOrder(getApplicationContext());
        showHiddenFiles = SPUtils.isShowingHiddenFiles(getApplicationContext());

        refreshList(getApplicationContext());

        fileChooser.setOnItemClickListener(listener);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        } else {
            if (item.getItemId() == 0) {
                isTimeSorted = !isTimeSorted;
                SPUtils.setTimeSorted(getApplicationContext(), isTimeSorted);
                refreshList(getApplicationContext());
            } else if (item.getItemId() == 1) {
                isNormalOrder = !isNormalOrder;
                SPUtils.setNormalOrder(getApplicationContext(), isNormalOrder);
                refreshList(getApplicationContext());
            } else if (item.getItemId() == 2) {
                showHiddenFiles = !showHiddenFiles;
                SPUtils.setShowingHiddenFiles(getApplicationContext(), showHiddenFiles);
                refreshList(getApplicationContext());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, 0, 0, isTimeSorted ? R.string.nameSortedKey : R.string.timeSortedKey);
        menu.add(0, 1, 1, isNormalOrder ? R.string.normalOrderKey : R.string.reverseOrderKey);
        menu.add(0, 2, 2, showHiddenFiles ? R.string.hideHiddenFilesKey : R.string.showHiddenFilesKey);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, 0, 0, isTimeSorted ? R.string.nameSortedKey : R.string.timeSortedKey);
        menu.add(0, 1, 1, isNormalOrder ? R.string.normalOrderKey : R.string.reverseOrderKey);
        menu.add(0, 2, 2, showHiddenFiles ? R.string.hideHiddenFilesKey : R.string.showHiddenFilesKey);
        return super.onPrepareOptionsMenu(menu);
    }

    public void refreshList(Context c) {
        ListView fileChooser = findViewById(R.id.file_select_lv);
        TextView PathView = findViewById(R.id.file_select_dir_tv);
        String[] from = {"img", "text"};
        int[] to = {R.id.file_select_item_iv, R.id.file_select_item_tv};
        ArrayList<Map<String, Object>> files = new ArrayList<>();

        for (File f : Objects.requireNonNull(CurrentPath.listFiles())) {
            if (showHiddenFiles || !f.isHidden()) {
                HashMap<String, Object> file = new HashMap<>();
                if (f.isFile()) {
                    String[] split = f.getName().split("\\.");
                    if (f.getName().equals("MDATA") || f.getName().equals("GDATA") || f.getName().equals("RDATA")) {
                        file.put("img", R.drawable.save_wii);
                    } else if (split.length > 1 && (split[split.length - 1].equals("sav") || split[split.length - 1].equals("dsv"))) {
                        file.put("img", R.drawable.save_ds);
                    } else if (split.length > 1 && split[split.length - 1].equals("mio")) {
                        file.put("img", R.mipmap.ic_launcher_foreground);
                    } else {
                        file.put("img", R.drawable.baseline_file_open_24);
                    }
                } else if (f.isDirectory()) {
                    file.put("img", R.drawable.directory);
                }
                file.put("text", f.getName());
                file.put("date", f.lastModified());
                files.add(file);
            }
        }
        if (isTimeSorted) {
            Collections.sort(files, (o1, o2) -> {
                Comparator<Object> comparator = Collator.getInstance(Locale.getDefault());
                return isNormalOrder ? comparator.compare(o1.get("text"), o2.get("text")) : -comparator.compare(o1.get("text"), o2.get("text"));
            });
        } else {
            Collections.sort(files, (o1, o2) -> (isNormalOrder ? (long) o1.get("date") > (long) o2.get("date") ? 1 : -1 : (long) o1.get("date") > (long) o2.get("date") ? -1 : 1));
        }
        HashMap<String, Object> parentFolder = new HashMap<>();
        parentFolder.put("img", R.drawable.exit);
        parentFolder.put("text", "..");
        parentFolder.put("date", 0);
        files.add(0, parentFolder);
        fileChooser.setAdapter(new SimpleAdapter(c, files, R.layout.file_select_item, from, to));
        PathView.setText(CurrentPath.getAbsolutePath());
    }
}