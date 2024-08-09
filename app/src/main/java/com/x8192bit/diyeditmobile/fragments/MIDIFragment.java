package com.x8192bit.diyeditmobile.fragments;

import static com.x8192bit.diyeditmobile.fragments.MIDIFragment.egm;
import static com.x8192bit.diyeditmobile.fragments.MIDIFragment.em;
import static com.x8192bit.diyeditmobile.fragments.MIDIFragment.is_game;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xperia64.diyedit.ExportGameMidi;
import com.xperia64.diyedit.ExportMidi;
import com.xperia64.diyedit.FileByteOperations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import x8192bit.diyeditmobile.R;


public class MIDIFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_IS_GAME = "is_game";
    public static Boolean is_game;
    static ExportMidi em;
    static ExportGameMidi egm;
    private String name;
    private boolean playing = false;
    private boolean started = false;

    public MIDIFragment() {

    }

    public static MIDIFragment newInstance(String name, Boolean is_game) {
        MIDIFragment fragment = new MIDIFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putBoolean(ARG_IS_GAME, is_game);
        fragment.setArguments(args);
        return fragment;
    }

    public static String copyRawToTempFile(Context c, @RawRes int ResID, String prefix, String suffix) throws IOException {
        InputStream is = c.getResources().openRawResource(ResID);
        File f = File.createTempFile(prefix, suffix, c.getFilesDir());
        //File f = File.createTempFile(prefix, suffix, requireContext().getCacheDir());
        /*
        if(f.exists()){
            //TODO
            // if playable, then continue;
            // if play failed, then recopy
        }

         */
        try (FileOutputStream os = new FileOutputStream(f)) {
            int readBytes;
            byte[] buffer = new byte[4096];
            while ((readBytes = is.read(buffer)) != -1) {
                os.write(buffer, 0, readBytes);
            }
        }
        return f.getAbsolutePath();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (is_game) {
            if (!egm.paused() && !egm.playing() && started) {
                started = false;
                playing = false;
            }
            if (started) {
                egm.Stop();
            }
        } else {
            if (!em.paused() && !em.playing() && started) {
                started = false;
                playing = false;
            }
            if (started) {
                em.Stop();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            is_game = getArguments().getBoolean(ARG_IS_GAME);
        }
        if (is_game) {
            egm = new ExportGameMidi(FileByteOperations.read(name));
        } else {
            em = new ExportMidi(FileByteOperations.read(name));
        }
    }

    @Deprecated
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageButton stopButton = requireView().findViewById(R.id.midi_stop_btn);
        ImageButton playPauseButton = requireView().findViewById(R.id.midi_play_pause_btn);
        FloatingActionButton exportButton = requireView().findViewById(R.id.midi_save_btn);
        SeekBar timeBar = requireView().findViewById(R.id.midi_progress_bar);
        stopButton.setOnClickListener(v -> {
            if (is_game) {
                if (!egm.paused() && !egm.playing() && started) {
                    started = false;
                    playing = false;
                }

                if (started) {
                    egm.Stop();
                }
            } else {
                if (!em.paused() && !em.playing() && started) {
                    started = false;
                    playing = false;
                }
                if (started) {
                    em.Stop();
                }
            }

            playPauseButton.setImageResource(R.drawable.baseline_play_arrow_24);
        });
        playPauseButton.setOnClickListener(v -> {
            new PlayThread().setContext(requireContext()).start();

            //TODO AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH

            /* TRADITIONAL SOLUTION
            if (is_game) {
                if (!egm.paused() && !egm.playing() && started) {
                    started = false;
                    playing = false;
                }
                if (!playing && !started) {
                    playing = true;
                    started = true;
                    playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    new PlayThread().start(getContext(), timeBar, timeView);

                } else if (!playing && started) {
                    playing = true;
                    playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    egm.PlayPause();
                } else if (playing) {
                    playing = false;
                    playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    egm.PlayPause();
                }
            } else {
                if (!em.paused() && !em.playing() && started) {
                    started = false;
                    playing = false;
                }
                if (!playing && !started) {
                    playing = true;
                    started = true;
                    playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    new PlayThread().start(getContext(), timeBar, timeView);

                } else if (!playing && started) {
                    playing = true;
                    playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    em.PlayPause();
                } else if (playing) {
                    playing = false;
                    playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    em.PlayPause();
                }
            }



 */
        });

        exportButton.setOnClickListener(v -> {
            /*StorageChooser chooser = new StorageChooser.Builder()
                    .withActivity(getActivity())
                    .withFragmentManager(requireActivity().getFragmentManager())
                    .withMemoryBar(true)
                    .allowCustomPath(true)
                    .setType(StorageChooser.DIRECTORY_CHOOSER)
                    .build();
            chooser.show();
            chooser.setOnSelectListener(pathExtract -> {
                EditText fileNameEdit = new EditText(getContext());
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.exportFileNameSetKey)
                        .setCancelable(true)
                        .setView(fileNameEdit)
                        .setPositiveButton(R.string.okKey, (dialog, which) -> {
                            String fileName = fileNameEdit.getText().toString();
                            if (!fileName.toLowerCase(Locale.US).endsWith(".mid")) {
                                fileName += ".mid";
                            }
                            String pathName = pathExtract + "//" + fileName;


                        })
                        .show();
            });*/
        });
        timeBar.setEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.midi_fragment, container, true);
    }
}

class PlayThread extends Thread {

    static {
        System.loadLibrary("midi-interface");
    }

    Context c = null;

    public native String PlayMIDIFile(String MIDIFilePath, String SoundFontFilePath);

    public PlayThread setContext(Context context) {
        c = context;
        return this;
    }

    @Override
    public void run() {
        super.run();
        try {
            File f = File.createTempFile("midi", "mid", c.getCacheDir());
            if (is_game) {
                egm.export(f.getAbsolutePath(), false);
            } else {
                em.export(f.getAbsolutePath());
            }

            // TODO: OH WE HAVE ASS(ET)LOADER NOW SO A LOT OF WORK IS NEED HERE
            // AHH

            String soundFontPath = MIDIFragment.copyRawToTempFile(c, R.raw.wwdiy_soundfont, "soundfont", "sf2");

            PlayMIDIFile(f.getAbsolutePath(), soundFontPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
