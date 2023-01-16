package com.x8192Bit.DIYEdit_Mobile.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codekidlabs.storagechooser.StorageChooser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xperia64.diyedit.ExportGameMidi;
import com.xperia64.diyedit.ExportMidi;
import com.xperia64.diyedit.FileByteOperations;

import org.jfugue.Player;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import x8192Bit.DIYEdit_Mobile.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MIDIFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MIDIFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static String ARG_NAME = "name";
    private static String ARG_IS_GAME = "is_game";

    // TODO: Rename and change types of parameters
    private String name;
    private Boolean is_game;
    private Player p;
    private ExportGameMidi egm;
    private ExportMidi em;

    public MIDIFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name    Parameter 1.
     * @param is_game Parameter 2.
     * @return A new instance of fragment MIDIFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MIDIFragment newInstance(String name, Boolean is_game) {
        MIDIFragment fragment = new MIDIFragment();
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

        if (is_game) {
            egm = new ExportGameMidi(FileByteOperations.read(name));
        } else {
            em = new ExportMidi(FileByteOperations.read(name));
        }
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageButton stopButton = getView().findViewById(R.id.stopButton);
        ImageButton playPauseButton = getView().findViewById(R.id.playpauseButton);
        FloatingActionButton exportButton = getView().findViewById(R.id.exportMIDIButton);
        SeekBar timeBar = getView().findViewById(R.id.MIDIProgressBar);
        stopButton.setOnClickListener(v -> {
            if (is_game) {
                egm.Stop();
            } else {
                em.Stop();
            }
        });
        playPauseButton.setOnClickListener(v -> {
            if (is_game) {
                egm.PlayPause();
                if (egm.playing()) {
                    playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                } else {
                    playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24);
                }
            } else {
                em.PlayPause();
                if (em.playing()) {
                    playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                } else {
                    playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24);
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
                            if (!fileName.toLowerCase(Locale.US).endsWith(".mio")) {
                                fileName += ".mio";
                            }
                            String pathName = pathExtract + "//" + fileName;
                            try {
                                new File(pathName).createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "NOOOOOOOOOOO!!!!", Toast.LENGTH_SHORT).show();
                            }
                            if (is_game) {
                                egm.export(pathName, true);
                            } else {
                                em.export(pathName, true);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_midi, container, false);
    }
}