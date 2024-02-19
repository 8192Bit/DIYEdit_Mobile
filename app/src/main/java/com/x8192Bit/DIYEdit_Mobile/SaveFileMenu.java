package com.x8192Bit.DIYEdit_Mobile;

import static com.x8192Bit.DIYEdit_Mobile.MainActivity.CHOOSE_RESULT;
import static com.x8192Bit.DIYEdit_Mobile.MainActivity.CHOOSE_TYPE;
import static com.x8192Bit.DIYEdit_Mobile.fragments.SaveEditFragment.IS_IMPORT_MIO;
import static com.x8192Bit.DIYEdit_Mobile.fragments.SaveEditFragment.IS_SAVE_EDIT;
import static com.x8192Bit.DIYEdit_Mobile.fragments.SaveEditFragment.SAVE_EDIT_COUNT;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.x8192Bit.DIYEdit_Mobile.fragments.BGViewFragment;
import com.x8192Bit.DIYEdit_Mobile.fragments.MIDIFragment;
import com.x8192Bit.DIYEdit_Mobile.fragments.MetadataEditFragment;
import com.x8192Bit.DIYEdit_Mobile.fragments.SaveEditFragment;
import com.x8192Bit.DIYEdit_Mobile.fragments.UnlockFragment;
import com.xperia64.diyedit.FileByteOperations;
import com.xperia64.diyedit.metadata.Metadata;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import x8192Bit.DIYEdit_Mobile.R;

public class SaveFileMenu extends AppCompatActivity {

    public static final String OPEN_FILE_CHOOSE_ACTIVITY = "com.x8192Bit.DIYEdit_Mobile.OPEN_FILE_CHOOSE_ACTIVITY";
    public static final String FILE_CHOOSE_ACTIVITY_RESULT = "com.x8192Bit.DIYEdit_Mobile.FILE_CHOOSE_ACTIVITY_RESULT";
    public static final String FILE_CHOOSE_REAL_PATH = "com.x8192Bit.DIYEdit_Mobile.FILE_CHOOSE_REAL_PATH";
    byte[] file;
    int savetype = 0;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), OPEN_FILE_CHOOSE_ACTIVITY)) {
                Intent i = new Intent(SaveFileMenu.this, FileChooseActivity.class);
                i.putExtra(CHOOSE_TYPE, FileChooseActivity.CHOOSE_FILE);
                i.putExtra(IS_SAVE_EDIT, intent.getBooleanExtra(IS_SAVE_EDIT, false));
                if (intent.getBooleanExtra(IS_SAVE_EDIT, false)) {
                    i.putExtra(IS_IMPORT_MIO, intent.getBooleanExtra(IS_IMPORT_MIO, false));
                    i.putExtra(SAVE_EDIT_COUNT, intent.getIntExtra(SAVE_EDIT_COUNT, 0));
                }
                startActivityForResult(i, 114514);
            }
        }
    };
    TabLayout tabs = null;
    ImageView SaveIcon = null;
    ViewPager2 vp2 = null;
    ArrayList<String> titleList = new ArrayList<>();
    int mio_type = 0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String path = intent.getStringExtra(MainActivity.REAL_PATH);
        initEnable(path);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        registerBroadcastReceiver();
    }

    public void initEnable(String filePath) {
        if (filePath != null) {
            try {
                file = FileByteOperations.read(filePath);
                int length = file.length;
                ArrayList<Fragment> fl = new ArrayList<>();
                if (length == 33554432 || length == 33554554 || length == 33566720 || length == 16777216) {
                    performDSSaveDATA(fl, filePath);
                } else {
                    if (file.length == 4719808) {
                        performGDATA(fl, filePath);
                    } else if (length == 591040) {
                        performRDATA(fl, filePath);
                    } else if (length == 1033408) {
                        performMDATA(fl, filePath);
                    } else if (file.length == 6438592) {
                        performWiiCompressedDATA(fl, filePath);
                    }
                }

                // Only detect when the file is not a save file
                if (savetype == 0) {
                    if (file.length == 65536) {
                        performGAME(fl, filePath);
                    } else if (file.length == 8192) {
                        performRECORD(fl, filePath);
                    } else if (file.length == 14336) {
                        performMANGA(fl, filePath);
                    }
                }
                if (savetype != 0 || mio_type != 0) {
                    updateFileHistory(filePath);
                    vp2.setAdapter(new FragmentStateAdapter(this) {
                        @NonNull
                        @Override
                        public Fragment createFragment(int position) {
                            return fl.get(position);
                        }

                        @Override
                        public int getItemCount() {
                            return fl.size();
                        }
                    });
                    new TabLayoutMediator(tabs, vp2, (tab, position) -> tab.setText(titleList.get(position))).attach();

                } else {
                    fileUnavailableAlert();
                }
            } catch (Exception e) {

            }
        } else {
            fileUnavailableAlert();
        }
    }

    @SuppressLint("SetTextI18n")
    private void performGDATA(ArrayList<Fragment> fl, String filePath) {
        savetype |= 1;
        setMenuType(contentViewType.SAVE);
        TextView saveView = findViewById(R.id.saveView);
        saveView.setText(getText(R.string.wiiSaveKey) + " (GDATA)");
        TabLayout.Tab t0 = tabs.newTab();
        titleList.add(getResources().getString(R.string.editGameKey));
        tabs.addTab(t0, 0);
        fl.add(SaveEditFragment.newInstance(filePath, 0));
    }

    @SuppressLint("SetTextI18n")
    private void performRDATA(ArrayList<Fragment> fl, String filePath) {
        savetype |= 2;
        setMenuType(contentViewType.SAVE);
        TextView saveView = findViewById(R.id.saveView);
        saveView.setText(getText(R.string.wiiSaveKey) + " (RDATA)");
        TabLayout.Tab t0 = tabs.newTab();
        titleList.add(getResources().getString(R.string.editRecordKey));
        tabs.addTab(t0, 0);
        fl.add(SaveEditFragment.newInstance(filePath, 1));
    }

    @SuppressLint("SetTextI18n")
    private void performMDATA(ArrayList<Fragment> fl, String filePath) {
        savetype |= 4;
        setMenuType(contentViewType.SAVE);
        TextView saveView = findViewById(R.id.saveView);
        saveView.setText(getText(R.string.wiiSaveKey) + " (MDATA)");
        TabLayout.Tab t0 = tabs.newTab();
        titleList.add(getResources().getString(R.string.editMangaKey));
        tabs.addTab(t0, 0);
        fl.add(SaveEditFragment.newInstance(filePath, 2));
    }

    private void performWiiCompressedDATA(ArrayList<Fragment> fl, String filePath) {
        savetype |= 7;
        setMenuType(contentViewType.SAVE);
        setContentView(R.layout.activity_save_file_menu);
        tabs = findViewById(R.id.tabs);
        TabLayout.Tab t0 = tabs.newTab();
        TabLayout.Tab t1 = tabs.newTab();
        TabLayout.Tab t2 = tabs.newTab();
        titleList.add(getResources().getString(R.string.editGameKey));
        titleList.add(getResources().getString(R.string.editRecordKey));
        titleList.add(getResources().getString(R.string.editMangaKey));
        tabs.addTab(t0, 0);
        tabs.addTab(t1, 1);
        tabs.addTab(t2, 2);
        fl.add(SaveEditFragment.newInstance(filePath, 0));
        fl.add(SaveEditFragment.newInstance(filePath, 1));
        fl.add(SaveEditFragment.newInstance(filePath, 2));
    }

    private void setMenuType(contentViewType tvv) {
        if (tvv == contentViewType.MIO && mio_type != 0) {
            setContentView(R.layout.activity_mio_file_menu);
            vp2 = findViewById(R.id.mioViewPager);
        }
        if (tvv == contentViewType.SAVE && savetype != 0) {
            setContentView(R.layout.activity_save_file_menu);
            vp2 = findViewById(R.id.saveViewPager);
        }
        if (tabs == null) {
            tabs = findViewById(R.id.tabs);
        }
    }

    private void performGAME(ArrayList<Fragment> fl, String filePath) {
        mio_type = 3;
        setMenuType(contentViewType.MIO);
        TabLayout.Tab t0 = tabs.newTab();
        TabLayout.Tab t1 = tabs.newTab();
        TabLayout.Tab t2 = tabs.newTab();
        //TabLayout.Tab t3 = tabs.newTab();
        titleList.add(getResources().getString(R.string.metaDataEditKey));
        titleList.add(getResources().getString(R.string.viewBGKey));
        titleList.add(getResources().getString(R.string.midiToolsKey));
        //titleList.add(getResources().getString(R.string.exportAllOBJKey));
        tabs.addTab(t0, 0);
        tabs.addTab(t1, 1);
        tabs.addTab(t2, 2);
        //tabs.addTab(t3, 3);
        fl.add(MetadataEditFragment.newInstance(filePath, 0));
        fl.add(BGViewFragment.newInstance(filePath, true));
        fl.add(MIDIFragment.newInstance(filePath, true));
        setMioMetadataEntries(filePath);
    }

    private void performMANGA(ArrayList<Fragment> fl, String filePath) {
        mio_type = 2;
        setMenuType(contentViewType.MIO);
        TabLayout.Tab t0 = tabs.newTab();
        TabLayout.Tab t1 = tabs.newTab();
        titleList.add(getResources().getString(R.string.metaDataEditKey));
        titleList.add(getResources().getString(R.string.viewMangaKey));
        tabs.addTab(t0, 0);
        tabs.addTab(t1, 1);
        fl.add(MetadataEditFragment.newInstance(filePath, 2));
        fl.add(BGViewFragment.newInstance(filePath, false));
        setMioMetadataEntries(filePath);
    }

    private void performDSSaveDATA(ArrayList<Fragment> fl, String filePath) {
        savetype |= 8;
        setMenuType(contentViewType.SAVE);
        TextView saveView = findViewById(R.id.saveView);
        saveView.setText(R.string.dsSaveKey);
        SaveIcon = findViewById(R.id.SaveImage);
        SaveIcon.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.save_ds));
        tabs = findViewById(R.id.tabs);
        TabLayout.Tab t0 = tabs.newTab();
        TabLayout.Tab t1 = tabs.newTab();
        TabLayout.Tab t2 = tabs.newTab();
        TabLayout.Tab t3 = tabs.newTab();
        titleList.add(getResources().getString(R.string.editGameKey));
        titleList.add(getResources().getString(R.string.editRecordKey));
        titleList.add(getResources().getString(R.string.editMangaKey));
        titleList.add(getResources().getString(R.string.unlockKey));
        tabs.addTab(t0, 0);
        tabs.addTab(t1, 1);
        tabs.addTab(t2, 2);
        tabs.addTab(t3, 3);
        fl.add(SaveEditFragment.newInstance(filePath, 0));
        fl.add(SaveEditFragment.newInstance(filePath, 1));
        fl.add(SaveEditFragment.newInstance(filePath, 2));
        fl.add(UnlockFragment.newInstance(filePath));
    }


    private void updateFileHistory(String realPath) {
        SharedPreferences sp = SaveFileMenu.this.getSharedPreferences("com.x8192Bit.DIYEdit_Mobile_preferences", MODE_PRIVATE);
        try {
            int historyCount = Integer.parseInt(sp.getString("maxHistoryCount", "10"));
            SharedPreferences.Editor ed = sp.edit();
            String history = sp.getString("history", null);
            StringBuilder sb = new StringBuilder();
            if (history != null) {
                ArrayList<String> historyArray = new ArrayList<String>(Arrays.asList(history.split(",")));
                boolean isDuplicated = false;
                for (String s : historyArray) {
                    if (s.equals(realPath)) {
                        isDuplicated = true;
                        break;
                    }
                }
                if (!isDuplicated) {
                    historyArray.add(0, realPath);
                }
                if (historyArray.size() > historyCount) {
                    do {
                        historyArray.remove(historyCount - 1);
                    } while (historyArray.size() == historyCount);
                }
                for (int i = 0; i < historyArray.size(); i++) {
                    sb.append(historyArray.get(i)).append(",");
                }
            } else {
                sb.append(realPath);
            }
            ed.putString("history", sb.toString());
            ed.apply();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), R.string.numberRequiredKey, Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    private void performRECORD(ArrayList<Fragment> fl, String filePath) {
        mio_type = 1;
        setMenuType(contentViewType.MIO);
        TabLayout.Tab t0 = tabs.newTab();
        TabLayout.Tab t1 = tabs.newTab();
        titleList.add(getResources().getString(R.string.metaDataEditKey));
        titleList.add(getResources().getString(R.string.midiToolsKey));
        tabs.addTab(t0, 0);
        tabs.addTab(t1, 1);
        fl.add(MetadataEditFragment.newInstance(filePath, 1));
        fl.add(MIDIFragment.newInstance(filePath, false));
        setMioMetadataEntries(filePath);
    }

    private void fileUnavailableAlert() {
        new AlertDialog.Builder(SaveFileMenu.this)
                .setTitle(R.string.warningKey)
                .setMessage(R.string.couldNotReadFileKey)
                .setCancelable(false)
                .setNeutralButton(R.string.backKey, (dialog, which) -> SaveFileMenu.this.finish())
                .show();
    }

    enum contentViewType {
        MIO,
        SAVE
    }

    @SuppressLint("DefaultLocale")
    private void setMioMetadataEntries(String filePath) {
        TextView MTimeLabel = findViewById(R.id.timeValue);
        TextView MAuthorLabel = findViewById(R.id.authorValue);
        TextView MCompanyLabel = findViewById(R.id.companyValue);
        TextView MNameLabel = findViewById(R.id.nameValue);
        Metadata m = new Metadata(filePath);
        MCompanyLabel.setText(m.getBrand());
        MAuthorLabel.setText(m.getCreator());
        MNameLabel.setText(m.getName());
        DateTime date = new DateTime(2000, 1, 1, 0, 0, 0, 0);
        date = date.plusDays(m.getTimestamp());
        MTimeLabel.setText(String.format("%04d-%02d-%02d", date.getYear(), date.getMonthOfYear(), date.getDayOfMonth()));
    }

    public void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(OPEN_FILE_CHOOSE_ACTIVITY);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 114514 && resultCode == 1919810) {
            if (data != null) {
                Intent intent = new Intent(FILE_CHOOSE_ACTIVITY_RESULT);
                intent.putExtra(FILE_CHOOSE_REAL_PATH, data.getStringExtra(CHOOSE_RESULT));
                intent.putExtra(IS_SAVE_EDIT, data.getBooleanExtra(IS_SAVE_EDIT, false));
                if (data.getBooleanExtra(IS_SAVE_EDIT, false)) {
                    intent.putExtra(IS_IMPORT_MIO, data.getBooleanExtra(IS_IMPORT_MIO, false));
                    intent.putExtra(SAVE_EDIT_COUNT, data.getIntExtra(SAVE_EDIT_COUNT, 0));
                }
                sendBroadcast(intent);
            } else {
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("嘿嘿！！")
                        .setMessage("安卓崩喽！安卓崩喽！！！1111")
                        .show();
            }
        }
    }
}