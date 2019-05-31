package com.example.appingles.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.appingles.R;
import com.example.appingles.adapter.AdapterFrases;
import com.example.appingles.modelo.Frases;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView meuRecyclerView;
    private List<Frases> listaFrases;
    private DatabaseReference firebase;

    public HomeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_home, container, false);
        meuRecyclerView = view.findViewById(R.id.home_recyclerview);
        AdapterFrases adapterFrases = new AdapterFrases(getContext(), listaFrases);
        meuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        meuRecyclerView.setAdapter(adapterFrases);
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listaFrases = new ArrayList<>();
        listaFrases.add(new Frases( "I Love You", "Eu Amo Voce"));
        listaFrases.add(new Frases( "Testing", "Tentando agora em portugues"));
        listaFrases.add(new Frases( "Hey man", "Ei cara!"));
        listaFrases.add(new Frases( "You can help me", "Voçe pode me ajudar"));
        listaFrases.add(new Frases( "Hello", "Olá"));
        listaFrases.add(new Frases( "how are you", "Como vc está"));
        listaFrases.add(new Frases( "Nice meet you", "Prazer em te conhecer"));
        listaFrases.add(new Frases( "My friends", "Meu Amigo"));
    }
}
