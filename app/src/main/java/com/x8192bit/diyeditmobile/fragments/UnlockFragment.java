package com.x8192bit.diyeditmobile.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xperia64.diyedit.FileByteOperations;
import com.xperia64.diyedit.saveutils.SaveHandler;

import x8192Bit.DIYEdit_Mobile.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnlockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnlockFragment extends Fragment {

    private static final String ARG_NAME = "name";
    Button unlockGames = null;
    Button unlockRecords = null;
    Button unlockMangas = null;
    Button unlockMedals = null;

    private String name;

    public UnlockFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name name of the file.
     * @return A new instance of fragment UnlockFragment.
     */
    public static UnlockFragment newInstance(String name) {
        UnlockFragment fragment = new UnlockFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unlockGames = view.findViewById(R.id.unlock_game_btn);
        unlockRecords = view.findViewById(R.id.unlock_record_btn);
        unlockMangas = view.findViewById(R.id.unlock_manga_btn);
        unlockMedals = view.findViewById(R.id.unlock_medal_btn);
        unlockGames.setOnClickListener(this::unlockGame);
        unlockRecords.setOnClickListener(this::unlockRecord);
        unlockMangas.setOnClickListener(this::unlockManga);
        unlockMedals.setOnClickListener(this::unlockMedal);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.unlock_fragment, container, false);
    }

    public void unlockGame(View v) {
        SaveHandler s = new SaveHandler(name);
        FileByteOperations.write(s.unlockMedals(), name);
    }

    public void unlockRecord(View v) {
        SaveHandler s = new SaveHandler(name);
        FileByteOperations.write(s.unlockMedals(), name);
    }

    public void unlockManga(View v) {
        SaveHandler s = new SaveHandler(name);
        FileByteOperations.write(s.unlockMedals(), name);
    }

    public void unlockMedal(View v) {
        SaveHandler s = new SaveHandler(name);
        FileByteOperations.write(s.unlockMedals(), name);
    }
}