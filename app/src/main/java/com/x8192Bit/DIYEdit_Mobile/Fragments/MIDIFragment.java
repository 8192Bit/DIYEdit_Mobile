package com.x8192Bit.DIYEdit_Mobile.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jfugue.Player;

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
    private Boolean isPlaying;
    private String name;
    private Boolean is_game;
    private Player p;

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
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getView().findViewById(R.id.stopButton).setOnClickListener(v -> {
            isPlaying = false;
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_midi, container, false);
    }
}