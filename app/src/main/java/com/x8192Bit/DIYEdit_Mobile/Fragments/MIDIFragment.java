package com.x8192Bit.DIYEdit_Mobile.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codekidlabs.storagechooser.StorageChooser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xperia64.diyedit.ExportGameMidi;
import com.xperia64.diyedit.ExportMidi;
import com.xperia64.diyedit.FileByteOperations;

import java.util.Locale;

import x8192Bit.DIYEdit_Mobile.R;

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
        ImageButton stopButton = getView().findViewById(R.id.stopButton);
        ImageButton playPauseButton = getView().findViewById(R.id.playpauseButton);
        FloatingActionButton exportButton = getView().findViewById(R.id.exportMIDIButton);
        SeekBar timeBar = getView().findViewById(R.id.MIDIProgressBar);
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

            playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        });
        playPauseButton.setOnClickListener(v -> {
            if (is_game) {
                if (!egm.paused() && !egm.playing() && started) {
                    started = false;
                    playing = false;
                }
                if (!playing && !started) {
                    playing = true;
                    started = true;
                    playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    new PlayThread().start();

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
                    new PlayThread().start();

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

        });
        exportButton.setOnClickListener(v -> {
            StorageChooser chooser = new StorageChooser.Builder()
                    .withActivity(getActivity())
                    .withFragmentManager(getActivity().getFragmentManager())
                    .withMemoryBar(true)
                    .allowCustomPath(true)
                    .setType(StorageChooser.DIRECTORY_CHOOSER)
                    .build();
            chooser.show();
            chooser.setOnSelectListener(pathExtract -> {
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
                            String pathName = pathExtract + "//" + fileName;

                            if (is_game) {
                                if (!egm.paused() && !egm.playing() && started) {
                                    started = false;
                                    playing = false;
                                }
                                if (started) {
                                    egm.Stop();
                                }
                                egm.export(pathName, false);
                            } else {
                                if (!em.paused() && !em.playing() && started) {
                                    started = false;
                                    playing = false;
                                }
                                if (started) {
                                    em.Stop();
                                }
                                em.export(pathName, false);
                            }
                        })
                        .show();
            });
        });
        timeBar.setEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_midi, container, true);
    }
}


class PlayThread extends Thread {
    public void run() {
        if (MIDIFragment.is_game)
            MIDIFragment.egm.export("", true);
        else
            MIDIFragment.em.export("", true);
    }
}
