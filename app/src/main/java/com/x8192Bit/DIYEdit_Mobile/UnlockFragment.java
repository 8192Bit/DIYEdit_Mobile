package com.x8192Bit.DIYEdit_Mobile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xperia64.diyedit.FileByteOperations;
import com.xperia64.diyedit.saveutils.SaveHandler;

import x8192Bit.DIYEdit_Mobile.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnlockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnlockFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NAME = "name";

    // TODO: Rename and change types of parameters
    private String name;
    private SaveHandler s;

    public UnlockFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name name of the file.
     * @return A new instance of fragment UnlockFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UnlockFragment newInstance(String name) {
        UnlockFragment fragment = new UnlockFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
        }
        s = new SaveHandler(name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unlock, container, false);
    }

    public void unlockGame(View v){
        FileByteOperations.write(s.unlockMedals(),name);
    }
    public void unlockRecord(View v){
        FileByteOperations.write(s.unlockMedals(),name);
    }
    public void unlockManga(View v){
        FileByteOperations.write(s.unlockMedals(),name);
    }
    public void unlockMedal(View v){
        FileByteOperations.write(s.unlockMedals(),name);
    }
}