package com.x8192Bit.DIYEdit_Mobile.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import x8192Bit.DIYEdit_Mobile.R;

public class FileSelectFragment extends Fragment {

    public static final int CHOOSE_FILE = 0;
    public static final int CHOOSE_DIR = 1;
    private static final String ARG_CHOOSE_TARGET = "chooseTarget";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private int chooseTarget;
    private String mParam2;

    public FileSelectFragment() {

    }

    public static FileSelectFragment newInstance(int cTarget, String param2) {
        FileSelectFragment fragment = new FileSelectFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CHOOSE_TARGET, cTarget);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chooseTarget = getArguments().getInt(ARG_CHOOSE_TARGET);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void refreshList(Context c, View v) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshList(getContext(), requireView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file_select, container, false);
    }
}