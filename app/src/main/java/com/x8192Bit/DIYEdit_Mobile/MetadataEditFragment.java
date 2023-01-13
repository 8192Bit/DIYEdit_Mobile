package com.x8192Bit.DIYEdit_Mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import x8192Bit.DIYEdit_Mobile.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MetadataEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MetadataEditFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_MIOTYPE = "miotype";

    // TODO: Rename and change types of parameters
    private String name;
    private int miotype;

    public MetadataEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name    name of the file.
     * @param miotype mio type.
     * @return A new instance of fragment MetadataEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MetadataEditFragment newInstance(String name, String miotype) {
        MetadataEditFragment fragment = new MetadataEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_MIOTYPE, miotype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            miotype = getArguments().getInt(ARG_MIOTYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_metadata, container, false);
    }
}