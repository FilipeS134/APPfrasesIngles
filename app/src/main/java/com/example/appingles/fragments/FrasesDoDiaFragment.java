package com.example.appingles.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appingles.R;
import com.example.appingles.adapter.AdapterFrases;
import com.example.appingles.config.ConfiguracaoFirebase;
import com.example.appingles.modelo.Frases;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrasesDoDiaFragment extends Fragment {

    private View view;
    private RecyclerView meuRecyclerView;
    private List<Frases> listaFrases;
    private List<Frases> frasesDia;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;


    public FrasesDoDiaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listaFrases = new ArrayList<>();
        frasesDia = new ArrayList<>();

        view = inflater.inflate(R.layout.fragment_frases_do_dia, container, false);
        meuRecyclerView = view.findViewById(R.id.frasesdia_recyclerview);
        final AdapterFrases adapterFrases = new AdapterFrases(getContext(), frasesDia);
        meuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        meuRecyclerView.setAdapter(adapterFrases);

        firebase = ConfiguracaoFirebase.getFirebase().child("frases");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // limpar lista
                listaFrases.clear();

                // lista frases
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Frases frases = dados.getValue( Frases.class);
                    listaFrases.add(frases);

                }
                Collections.shuffle(listaFrases);
                for (int i = 0; i < 5; i++){
                    frasesDia.add(listaFrases.get(i));
                }
                Collections.sort(frasesDia);
                adapterFrases.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };

        return view;
    }

}
