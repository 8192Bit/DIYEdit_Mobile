package com.x8192bit.diyeditmobile.fragments;

import static com.x8192bit.diyeditmobile.MainActivity.CHOOSE_RESULT;
import static com.x8192bit.diyeditmobile.fragments.SaveEditFragment.IS_IMPORT_MIO;
import static com.x8192bit.diyeditmobile.fragments.SaveEditFragment.IS_SAVE_EDIT;
import static com.x8192bit.diyeditmobile.fragments.SaveEditFragment.SAVE_EDIT_COUNT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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

import x8192Bit.DIYEdit_Mobile.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileSelectFragment extends android.app.DialogFragment {

    public static final int CHOOSE_FILE = 0;
    public static final int CHOOSE_DIRECTORY = 1;
    private static final String ARG_SELECT_TYPE = "select_type";
    File CurrentPath = null;
    private boolean isImportMIO;
    private boolean isSaveEdit;
    private int saveEditCount;
    private boolean isTimeSorted;
    private boolean isNormalOrder;
    private boolean showHiddenFiles;
    private int selectType;
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
                    if (CurrentPath.isFile() && selectType == CHOOSE_FILE) {
                        Intent intentResult = new Intent();
                        intentResult.putExtra(CHOOSE_RESULT, CurrentPath.getAbsolutePath());
                        intentResult.putExtra(IS_SAVE_EDIT, isSaveEdit);
                        if (isSaveEdit) {
                            intentResult.putExtra(IS_IMPORT_MIO, isImportMIO);
                            intentResult.putExtra(SAVE_EDIT_COUNT, saveEditCount);
                        }
                        //setResult(1919810, intentResult);
                        //finish();
                    }
                }
                refreshList(getActivity().getApplicationContext(), view);
            } catch (NullPointerException e) {
                CurrentPath = originalPath;
            } catch (Exception e) {
                new AlertDialog.Builder(getActivity().getApplicationContext())
                        .setTitle("What the fuck????!!")
                        .setMessage("GO FUCK YOURSELF!!!!!!!!!!!!!!!!!11").show();
                CurrentPath = originalPath;
            }
        }
    };

    // TODO 快点重构你这坨狗屎
    public FileSelectFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param select_type Parameter 1.
     * @return A new instance of fragment FileSelectFragment.
     */
    public static FileSelectFragment newInstance(int select_type) {
        FileSelectFragment fragment = new FileSelectFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SELECT_TYPE, select_type);
        fragment.setArguments(args);
        return fragment;
    }

    private static @NonNull HashMap<String, Object> getStringObjectHashMap(File f) {
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
        return file;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectType = getArguments().getInt(ARG_SELECT_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.file_select_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button selectDirectoryButton = view.findViewById(R.id.file_select_ok_btn);

        if (selectType == CHOOSE_FILE) {
            // When selecting a file, the OK button is useless so remove
            ((LinearLayout) view.findViewById(R.id.file_select_ll)).removeView(selectDirectoryButton);
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
//                    setResult(1919810, intentResult);
//                    this.finish();
                } catch (Exception e) {
                    new AlertDialog.Builder(getActivity().getApplicationContext())
                            .setTitle("What the fuck????!!")
                            .setMessage("GO FUCK YOURSELF!!!!!!!!!!!!!!!!!11").show();
                }
            });
        }

        // initialize the actionbar so that we can add some button to it then
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        CurrentPath = Environment.getExternalStorageDirectory();

        ListView fileChooser = view.findViewById(R.id.file_select_lv);

        isTimeSorted = SPUtils.isTimeSorted(getActivity().getApplicationContext());
        isNormalOrder = SPUtils.isNormalOrder(getActivity().getApplicationContext());
        showHiddenFiles = SPUtils.isShowingHiddenFiles(getActivity().getApplicationContext());

        refreshList(getActivity().getApplicationContext(), view);

        fileChooser.setOnItemClickListener(listener);
    }

    public void refreshList(Context c, View view) {
        ListView fileSelectView = view.findViewById(R.id.file_select_lv);
        TextView PathView = view.findViewById(R.id.file_select_dir_tv);

        String[] from = {"img", "text"};
        int[] to = {R.id.file_select_item_iv, R.id.file_select_item_tv};

        ArrayList<Map<String, Object>> files = new ArrayList<>();

        for (File f : Objects.requireNonNull(CurrentPath.listFiles())) {
            if (showHiddenFiles || !f.isHidden()) {
                HashMap<String, Object> file = getStringObjectHashMap(f);
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
        fileSelectView.setAdapter(new SimpleAdapter(c, files, R.layout.file_select_item, from, to));
        PathView.setText(CurrentPath.getAbsolutePath());
    }

}