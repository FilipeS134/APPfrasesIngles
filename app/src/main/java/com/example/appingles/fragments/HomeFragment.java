package com.example.appingles.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appingles.R;
import com.example.appingles.activity.MainActivity;
import com.example.appingles.adapter.AdapterFrases;
import com.example.appingles.config.ConfiguracaoFirebase;
import com.example.appingles.modelo.Frases;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView meuRecyclerView;
    private List<Frases> listaFrases;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;

    public HomeFragment(){

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

        view = inflater.inflate(R.layout.fragment_home, container, false);
        meuRecyclerView = view.findViewById(R.id.home_recyclerview);
        final AdapterFrases adapterFrases = new AdapterFrases(getContext(), listaFrases);
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
                Collections.sort(listaFrases);
                adapterFrases.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
