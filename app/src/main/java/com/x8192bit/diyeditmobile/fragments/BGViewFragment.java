package com.x8192bit.diyeditmobile.fragments;

import static com.x8192bit.diyeditmobile.SaveEditActivity.FILE_CHOOSE_ACTIVITY_RESULT;
import static com.x8192bit.diyeditmobile.SaveEditActivity.FILE_CHOOSE_REAL_PATH;
import static com.x8192bit.diyeditmobile.SaveEditActivity.OPEN_FILE_CHOOSE_ACTIVITY;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.xperia64.diyedit.editors.GameEdit;
import com.xperia64.diyedit.editors.MangaEdit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.Objects;

import x8192bit.diyeditmobile.R;

public class BGViewFragment extends Fragment {

    public static final String IS_SAVE_EDIT = "com.x8192bit.diyeditmobile.IS_SAVE_EDIT";
    private static final String ARG_NAME = "name";
    private static final String ARG_IS_GAME = "is_game";
    SwitchMaterial bp = null;
    SeekBar ms = null;
    private String name;
    private Boolean is_game;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), FILE_CHOOSE_ACTIVITY_RESULT)) {
                try {
                    SwitchMaterial bp = requireView().findViewById(R.id.bg_preview_sw);
                    SeekBar ms = requireView().findViewById(R.id.bg_manga_sb);
                    String path = intent.getStringExtra(FILE_CHOOSE_REAL_PATH);
                    EditText fileNameEdit = new EditText(getContext());
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.exportFileNameSetKey)
                            .setCancelable(true)
                            .setView(fileNameEdit)
                            .setPositiveButton(R.string.okKey, (dialog, which) -> {
                                String fileName = fileNameEdit.getText().toString();
                                if (!fileName.toLowerCase(Locale.US).endsWith(".png")) {
                                    fileName += ".png";
                                }
                                String pathName = path + "//" + fileName;

                                Bitmap b;
                                if (is_game) {
                                    if (!bp.isChecked()) {
                                        b = DrawGameBG(name);
                                    } else {
                                        b = DrawGamePreview(name);
                                    }
                                } else {
                                    b = DrawManga(ms.getProgress(), name);
                                }

                                try {
                                    FileOutputStream out = new FileOutputStream(pathName);
                                    if (b.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                                        try {
                                            out.flush();
                                            out.close();
                                            Toast.makeText(getContext(), R.string.exportSuccessKey, Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(getContext(), R.string.exportFailedKey, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }).show();
                } catch (NullPointerException e) {
                    new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                            .setTitle("嘿嘿！！")
                            .setMessage("安卓崩喽！安卓崩喽！！！1111")
                            .show();
                }
            }
        }
    };

    public BGViewFragment() {

    }

    public static BGViewFragment newInstance(String name, Boolean is_game) {
        BGViewFragment fragment = new BGViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putBoolean(ARG_IS_GAME, is_game);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bgview_fragment, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            is_game = getArguments().getBoolean(ARG_IS_GAME);
        }
        IntentFilter intentFilter = new IntentFilter(FILE_CHOOSE_ACTIVITY_RESULT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            requireActivity().registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    @SuppressLint("SetTextI18n")
    void refreshBG(View view) {
        PhotoView bg = view.findViewById(R.id.bg_pv);
        if (is_game) {
            SwitchMaterial bp = view.findViewById(R.id.bg_preview_sw);
            if (bp.isChecked()) {
                bg.setImageBitmap(DrawGamePreview(name));
            } else {
                bg.setImageBitmap(DrawGameBG(name));
            }
        } else {
            SeekBar ms = view.findViewById(R.id.bg_manga_sb);
            bg.setImageBitmap(DrawManga(ms.getProgress(), name));
        }
    }

    Bitmap DrawGamePreview(String name) {
        Bitmap b = Bitmap.createBitmap(96, 64, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        GameEdit e2 = new GameEdit(name);
        Paint p = new Paint();
        for (int y = 0; y < 64; y++) {
            for (int x = 0; x < 96; x++) {
                int cc = e2.getPreviewPixel(x, y);
                p.setColor(Color.rgb(r(cc), g(cc), b(cc)));
                c.drawPoint(x, y, p);
                x++;
            }
        }
        return b;
    }

    public Bitmap DrawGameBG(String file) {
        Bitmap b = Bitmap.createBitmap(192, 128, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        GameEdit ge = new GameEdit(file);
        Paint p = new Paint();
        for (int y = 0; y < 128; y++) {
            for (int x = 0; x < 192; x++) {
                int cc = ge.getBackgroundPixel(x, y);
                p.setColor(
                        cc == 0 ? Color.argb(255, 0, 0, 0)
                                : Color.argb(255, r(cc), g(cc), b(cc)));
                c.drawPoint(x, y, p);
            }
        }
        return b;
    }

    public Bitmap DrawManga(int page, String file) {
        Bitmap b = Bitmap.createBitmap(192, 128, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        MangaEdit me = new MangaEdit(file);
        Paint p = new Paint();
        for (int y = 0; y < 128; y++) {
            for (int x = 0; x < 192; x++) {
                if (me.getPixel((byte) page, x, y)) {
                    p.setColor(Color.rgb(0, 0, 0));
                } else {
                    p.setColor(Color.rgb(255, 255, 255));
                }
                c.drawPoint(x, y, p);
            }
        }
        return b;
    }

    public int r(int b) {
        switch (b) {
            case 2:
            case 14:
            case 11:
            case 3:
            case 5:
                return 255;
            case 4:
                return 198;
            case 6:
                return 206;
            case 7:
                return 16;
            case 8:
                return 41;
            case 9:
                return 8;
            case 10:
                return 115;
            case 12:
                return 128;
            case 13:
                return 192;
            case 1:
            default:
                return 0;
        }
    }

    public int g(int b) {
        switch (b) {
            case 2:
                return 223;
            case 3:
                return 174;
            case 4:
                return 73;
            case 6:
            case 8:
                return 105;
            case 7:
                return 199;
            case 9:
                return 150;
            case 10:
                return 215;
            case 11:
            case 14:
                return 255;
            case 12:
                return 128;
            case 13:
                return 192;
            case 1:
            case 5:
            default:
                return 0;
        }
    }

    public int b(int b) {
        switch (b) {
            case 2:
                return 156;
            case 3:
                return 49;
            case 6:
                return 239;
            case 7:
                return 206;
            case 8:
                return 198;
            case 9:
                return 82;
            case 10:
                return 57;
            case 11:
                return 90;
            case 12:
                return 128;
            case 13:
                return 192;
            case 14:
                return 255;
            case 1:
            case 4:
            case 5:
            default:
                return 0;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PhotoView bg = view.findViewById(R.id.bg_pv);
        ms = view.findViewById(R.id.bg_manga_sb);
        TextView tv = view.findViewById(R.id.bg_preview_tv);
        FloatingActionButton ss = view.findViewById(R.id.bg_save_btn);
        bp = view.findViewById(R.id.bg_preview_sw);
        if (is_game) {
            ((ViewGroup) ms.getParent()).removeView(ms);
            bp.setOnClickListener(v -> {
                refreshBG(requireView());
                tv.setText(bp.isChecked() ? getText(R.string.previewKey) : "BG ");
            });
            bg.setImageBitmap(DrawGameBG(name));
        } else {
            ((ViewGroup) bp.getParent()).removeView(bp);
            ((ViewGroup) tv.getParent()).removeView(tv);
            ms.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    refreshBG(requireView());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            bg.setImageBitmap(DrawManga(0, name));
        }
        ss.setOnClickListener(v -> {
                    Intent intent = new Intent(OPEN_FILE_CHOOSE_ACTIVITY);
                    intent.putExtra(IS_SAVE_EDIT, false);
                    requireActivity().sendBroadcast(intent);
                    // 事件处理请移步broadcastReceiver.onReceive
                }
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(broadcastReceiver);
    }


}