package com.x8192Bit.DIYEdit_Mobile.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codekidlabs.storagechooser.StorageChooser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
        SeekBar resize = view.findViewById(R.id.resizeSeekBar);
        FloatingActionButton ss = view.findViewById(R.id.SaveBGButton);
        ToggleButton bp = view.findViewById(R.id.BGPreviewToggle);
        resize.setMax(100);
        if (is_game) {
            ((ViewGroup) ms.getParent()).removeView(ms);
            bp.setOnClickListener(v -> refreshBG(requireView()));
            bg.setImageBitmap(DrawGameBG(192, 128, name));
        } else {
            ((ViewGroup) bp.getParent()).removeView(bp);
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
            bg.setImageBitmap(DrawManga(192, 128, 0, name));
        }
        ss.setOnClickListener(v -> SaveFileDialog(path -> {
            EditText fileNameEdit = new EditText(getContext());
            new AlertDialog.Builder(getContext())
                    .setTitle("Set export file name")
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
                                b = DrawGameBG(192, 128, name);
                            } else {
                                b = DrawGamePreview(96, 64, name);
                            }
                        } else {
                            b = DrawManga(192, 128, ms.getProgress(), name);
                        }

                        try {
                            FileOutputStream out = new FileOutputStream(pathName);
                            if (b.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                                try {
                                    out.flush();
                                    out.close();
                                    Toast.makeText(getContext(), R.string.exportSuccessKey, Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }).show();
        }));
        resize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
    }

    @SuppressLint("SetTextI18n")
    void refreshBG(View view) {
        //TODO resize
        ImageView bg = view.findViewById(R.id.BGView);
        SeekBar resize = view.findViewById(R.id.resizeSeekBar);
        TextView ratio = view.findViewById(R.id.resizeRatioTextView);
        double rate = (resize.getProgress() / 10.0) + 1;
        ratio.setText(String.valueOf(rate) + 'x');
        if (is_game) {
            ToggleButton bp = view.findViewById(R.id.BGPreviewToggle);
            if (bp.isChecked()) {
                bg.setImageBitmap(DrawGamePreview((int) (96 * rate), (int) (64 * rate), name));
            } else {
                bg.setImageBitmap(DrawGameBG((int) (192 * rate), (int) (128 * rate), name));
            }
        } else {
            SeekBar ms = view.findViewById(R.id.MangaSeekBar);
            bg.setImageBitmap(DrawManga((int) (192 * rate), (int) (128 * rate), ms.getProgress(), name));
        }
    }

    Bitmap DrawGamePreview(int width, int height, String name) {
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
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        new Canvas(result)
                .drawBitmap(b, new Rect(0, 0, 96, 64), new Rect(0, 0, width, height), p);
        return result;
    }

    public void SaveFileDialog(StorageChooser.OnSelectListener oc) {
        StorageChooser chooser = new StorageChooser.Builder()
                .withActivity(getActivity())
                .withFragmentManager(requireActivity().getFragmentManager())
                .withMemoryBar(true)
                .allowCustomPath(true)
                .setType(StorageChooser.DIRECTORY_CHOOSER)
                .build();
        chooser.show();
        chooser.setOnSelectListener(oc);
    }

    public Bitmap DrawGameBG(int width, int height, String file) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
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
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        new Canvas(result)
                .drawBitmap(b, new Rect(0, 0, 192, 128), new Rect(0, 0, width, height), p);

        return result;
    }

    public Bitmap DrawManga(int width, int height, int page, String file) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
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
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        new Canvas(result)
                .drawBitmap(b, new Rect(0, 0, 192, 128), new Rect(0, 0, width, height), p);

        return result;
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

}