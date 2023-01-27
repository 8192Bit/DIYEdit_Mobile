package com.x8192Bit.DIYEdit_Mobile.Fragments;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codekidlabs.storagechooser.StorageChooser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xperia64.diyedit.FileByteOperations;
import com.xperia64.diyedit.editors.GameEdit;
import com.xperia64.diyedit.editors.MangaEdit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import x8192Bit.DIYEdit_Mobile.R;

public class BGViewFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_IS_GAME = "is_game";

    private String name;
    private Boolean is_game;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            is_game = getArguments().getBoolean(ARG_IS_GAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bgview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView bg = view.findViewById(R.id.BGView);
        SeekBar ms = view.findViewById(R.id.MangaSeekBar);
        FloatingActionButton ss = view.findViewById(R.id.SaveBGButton);
        ToggleButton bp = view.findViewById(R.id.BGPreviewToggle);
        if (is_game) {
            ((ViewGroup) ms.getParent()).removeView(ms);
            bp.setOnClickListener((v) -> {
                if (bp.isChecked()) {
                    Bitmap b = Bitmap.createBitmap(96, 64, Bitmap.Config.RGB_565);
                    bg.setImageBitmap(DrawGamePreview(b, name));
                } else {
                    Bitmap b = Bitmap.createBitmap(192, 128, Bitmap.Config.RGB_565);
                    bg.setImageBitmap(DrawGameBG(b, name));
                }
            });
            Bitmap b = Bitmap.createBitmap(192, 128, Bitmap.Config.RGB_565);
            bg.setImageBitmap(DrawGameBG(b, name));
        } else {
            ((ViewGroup) bp.getParent()).removeView(bp);
            ms.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Bitmap b = Bitmap.createBitmap(192, 128, Bitmap.Config.RGB_565);
                    bg.setImageBitmap(DrawManga(b, progress, FileByteOperations.read(name)));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            Bitmap b = Bitmap.createBitmap(192, 128, Bitmap.Config.RGB_565);
            bg.setImageBitmap(DrawManga(b, 0, FileByteOperations.read(name)));
        }
        ss.setOnClickListener(v -> {
            SaveFileDialog(path -> {
                EditText fileNameEdit = new EditText(getContext());
                new AlertDialog.Builder(getContext())
                        .setTitle("Set export file name")
                        .setCancelable(true)
                        .setView(fileNameEdit)
                        .setPositiveButton(R.string.okKey, (dialog, which) -> {
                            String fileName = fileNameEdit.getText().toString();
                            if (!fileName.toLowerCase(Locale.US).endsWith(".mid")) {
                                fileName += ".mid";
                            }
                            String pathName = path + "//" + fileName;

                            Bitmap b = null;
                            if (is_game) {
                                if (bp.isChecked()) {
                                    b = Bitmap.createBitmap(192, 128, Bitmap.Config.RGB_565);
                                    b = DrawGameBG(b, name);
                                } else {
                                    b = Bitmap.createBitmap(96, 64, Bitmap.Config.RGB_565);
                                    b = DrawGamePreview(b, name);
                                }
                            } else {
                                b = Bitmap.createBitmap(192, 128, Bitmap.Config.RGB_565);
                                b = DrawManga(b, ms.getProgress(), FileByteOperations.read(name));
                            }

                            FileOutputStream out = null;
                            try {
                                out = new FileOutputStream(pathName);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            if (b.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                                try {
                                    out.flush();
                                    out.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).show();
            });
        });
    }

    Bitmap DrawGamePreview(Bitmap bm, String name) {
        Canvas c = new Canvas(bm);
        GameEdit e2 = new GameEdit(name);
        int x = 0;
        int y = 0;
        boolean done = false;
        while (!done) {
            Paint p = new Paint();
            int cc = e2.getPreviewPixel(x, y);
            p.setColor(Color.rgb(r(cc), g(cc), b(cc)));
            c.drawPoint(x, y, p);
            x++;
            if (x > 95) {
                y += 1;
                x = 0;
            }
            if (y > 63) {
                done = true;
                break;
            }
        }
        return bm;
    }

    public void SaveFileDialog(StorageChooser.OnSelectListener oc) {
        StorageChooser chooser = new StorageChooser.Builder()
                .withActivity(getActivity())
                .withFragmentManager(getActivity().getFragmentManager())
                .withMemoryBar(true)
                .allowCustomPath(true)
                .setType(StorageChooser.DIRECTORY_CHOOSER)
                .build();
        chooser.show();
        chooser.setOnSelectListener(oc);
    }

    public Bitmap DrawGameBG(Bitmap bm, String file) {
        Canvas c = new Canvas(bm);
        GameEdit ge = new GameEdit(file);
        int x = 0;
        int y = 0;
        boolean done = false;
        Paint p = new Paint();
        while (!done) {
            int cc = ge.getBackgroundPixel(x, y);
            if (cc != 0) {
                p.setColor(Color.argb(255, r(cc), g(cc), b(cc)));
            } else {
                p.setColor(Color.argb(255, 0, 0, 0));
            }
            c.drawPoint(x, y, p);
            x++;
            if (x > 191) {
                y += 1;
                x = 0;
            }
            if (y > 127) {
                done = true;
            }
        }
        return bm;
    }

    public Bitmap DrawManga(Bitmap bm, int page, byte[] b) {
        Canvas c = new Canvas(bm);
        int x = 0;
        int y = 0;
        boolean done = false;
        MangaEdit me = new MangaEdit(b);
        while (!done) {
            Paint p = new Paint();
            if (me.getPixel((byte) page, x, y)) {
                p.setColor(Color.rgb(0, 0, 0));
            } else {
                p.setColor(Color.rgb(255, 255, 255));
            }
            c.drawPoint(x, y, p);
            x++;
            if (x > 191) {
                y += 1;
                x = 0;
            }
            if (y > 127) {
                done = true;
                break;
            }
        }
        return bm;
    }

    public int r(int b) {
        switch (b) {
            case 1:
                return 0;
            case 2:
                return 255;
            case 3:
                return 255;
            case 4:
                return 198;
            case 5:
                return 255;
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
            case 11:
                return 255;
            case 12:
                return 128;
            case 13:
                return 192;
            case 14:
                return 255;
            default:
                return 0;
        }
    }

    public int g(int b) {
        switch (b) {
            case 1:
                return 0;
            case 2:
                return 223;
            case 3:
                return 174;
            case 4:
                return 73;
            case 5:
                return 0;
            case 6:
                return 105;
            case 7:
                return 199;
            case 8:
                return 105;
            case 9:
                return 150;
            case 10:
                return 215;
            case 11:
                return 255;
            case 12:
                return 128;
            case 13:
                return 192;
            case 14:
                return 255;
            default:
                return 0;
        }
    }

    public int b(int b) {
        switch (b) {
            case 1:
                return 0;
            case 2:
                return 156;
            case 3:
                return 49;
            case 4:
                return 0;
            case 5:
                return 0;
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
            default:
                return 0;
        }
    }

}