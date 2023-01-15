package com.x8192Bit.DIYEdit_Mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.x8192Bit.DIYEdit_Mobile.Fragments.MetadataEditFragment;
import com.x8192Bit.DIYEdit_Mobile.Fragments.SaveEditFragment;
import com.x8192Bit.DIYEdit_Mobile.Fragments.UnlockFragment;
import com.xperia64.diyedit.FileByteOperations;

import java.util.ArrayList;
import java.util.Arrays;

import x8192Bit.DIYEdit_Mobile.R;

public class SaveFileMenu extends AppCompatActivity {

    byte[] file;
    int savetype = 0;
    int miotype = 0;
    TabLayout tabs = null;
    TextView SSaveLabel = null;
    TextView MNameLabel = null;
    TextView MAuthorLabel = null;
    TextView MCompanyLabel = null;
    ImageView SaveIcon = null;
    TextView MTimeLabel = null;
    ViewPager2 vp2 = null;
    ArrayList<String> titleList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String path = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        initEnable(path);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish(); // back button
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param filePath Provide the filepath which got by the Sys FM.
     */
    @SuppressLint("SetTextI18n")// well at least, GDATA, MDATA and RDATA IS I18N
    public void initEnable(String filePath) {
        if (filePath != null) {
            file = FileByteOperations.read(filePath);
            ArrayList<Fragment> fl = new ArrayList<>();
            if (file.length == 4719808) {
                //GDATA
                savetype |= 1;
                setMenuType(contentViewType.SAVE);
                TextView saveView = findViewById(R.id.saveView);
                saveView.setText(getText(R.string.wiiSaveKey) + " (GDATA)");
                TabLayout.Tab t0 = tabs.newTab();
                titleList.add(getResources().getString(R.string.editGameKey));
                tabs.addTab(t0, 0);
                fl.add(SaveEditFragment.newInstance(filePath, 0));
            } else if (file.length == 591040) {
                //RDATA
                savetype |= 2;
                setMenuType(contentViewType.SAVE);
                TextView saveView = findViewById(R.id.saveView);
                saveView.setText(getText(R.string.wiiSaveKey) + " (RDATA)");
                TabLayout.Tab t0 = tabs.newTab();
                titleList.add(getResources().getString(R.string.editRecordKey));
                tabs.addTab(t0, 0);
                fl.add(SaveEditFragment.newInstance(filePath, 1));
            } else if (file.length == 1033408) {
                //MDATA
                savetype |= 4;
                setMenuType(contentViewType.SAVE);
                TextView saveView = findViewById(R.id.saveView);
                saveView.setText(getText(R.string.wiiSaveKey) + " (MDATA)");
                TabLayout.Tab t0 = tabs.newTab();
                titleList.add(getResources().getString(R.string.editMangaKey));
                tabs.addTab(t0, 0);
                fl.add(SaveEditFragment.newInstance(filePath, 2));
            } else if (file.length == 6438592) {
                //wii all-in-one save file?
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
            } else if (file.length == 33554432 || file.length == 33554554 || file.length == 33566720) {
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
            // Only detect when the file is not a save file
            if (savetype == 0) {
                if (file.length == 8192) {
                    miotype = 1;
                    setMenuType(contentViewType.MIO);
                    TabLayout.Tab t0 = tabs.newTab();
                    TabLayout.Tab t1 = tabs.newTab();
                    titleList.add(getResources().getString(R.string.metaDataEditKey));
                    titleList.add(getResources().getString(R.string.midiToolsKey));
                    tabs.addTab(t0);
                    tabs.addTab(t1);
                    fl.add(MetadataEditFragment.newInstance(filePath, 1));
                } else if (file.length == 14336) {
                    miotype = 2;
                    setMenuType(contentViewType.MIO);
                    TabLayout.Tab t0 = tabs.newTab();
                    TabLayout.Tab t1 = tabs.newTab();
                    titleList.add(getResources().getString(R.string.metaDataEditKey));
                    titleList.add(getResources().getString(R.string.viewMangaKey));
                    tabs.addTab(t0, 0);
                    tabs.addTab(t1, 1);
                    fl.add(MetadataEditFragment.newInstance(filePath, 2));
                } else if (file.length == 65536) {
                    miotype = 3;
                    setMenuType(contentViewType.MIO);
                    TabLayout.Tab t0 = tabs.newTab();
                    TabLayout.Tab t1 = tabs.newTab();
                    TabLayout.Tab t2 = tabs.newTab();
                    TabLayout.Tab t3 = tabs.newTab();
                    titleList.add(getResources().getString(R.string.metaDataEditKey));
                    titleList.add(getResources().getString(R.string.viewBGKey));
                    titleList.add(getResources().getString(R.string.midiToolsKey));
                    titleList.add(getResources().getString(R.string.exportAllOBJKey));
                    tabs.addTab(t0, 0);
                    tabs.addTab(t1, 1);
                    tabs.addTab(t2, 2);
                    tabs.addTab(t3, 3);
                    fl.add(MetadataEditFragment.newInstance(filePath, 0));
                }
            }
            vp2 = findViewById(R.id.saveViewPager);
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
            if (savetype != 0) {
                updateFileHistory(filePath);
            } else if (miotype != 0) {
                updateFileHistory(filePath);
                MTimeLabel = findViewById(R.id.timeValue);

            } else {
                updateFileHistory(filePath);
                fileUnavailableAlert();
            }
        } else {
            fileUnavailableAlert();
        }
    }

    private void setMenuType(contentViewType tvv) {
        if (tvv == contentViewType.MIO && miotype != 0) {
            setContentView(R.layout.activity_mio_file_menu);
        }
        if (tvv == contentViewType.SAVE && savetype != 0) {
            setContentView(R.layout.activity_save_file_menu);
        }
        if (tabs == null) {
            tabs = findViewById(R.id.tabs);
        }
    }

    private void updateFileHistory(String realPath) {
        SharedPreferences sp = SaveFileMenu.this.getSharedPreferences("com.x8192Bit.DIYEdit_Mobile_preferences", MODE_PRIVATE);
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
                }
            }
            if (!isDuplicated) {
                historyArray.add(0, realPath);
            }
            if (historyArray.size() > historyCount) {
                do {
                    historyArray.remove(historyCount);
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
    }

    private void setMioMetadataEntries() {

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
}