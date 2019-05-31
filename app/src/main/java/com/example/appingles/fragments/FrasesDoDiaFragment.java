package com.example.appingles.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appingles.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrasesDoDiaFragment extends Fragment {


    public FrasesDoDiaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frases_do_dia, container, false);
    }

}
