package com.example.appingles.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appingles.R;
import com.example.appingles.config.ConfiguracaoFirebase;
import com.example.appingles.modelo.Frases;
import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdapterFrases extends RecyclerView.Adapter<AdapterFrases.ViewHolder> {

    Context mContext;
    List<Frases> mData;

    public AdapterFrases(Context mContext, List<Frases> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.frases_layout,viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.ingles.setText(mData.get(i).getIngles());
        viewHolder.portugues.setText(mData.get(i).getPortugues());


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView ingles;
        private TextView portugues;
        private ImageView icon;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ingles = itemView.findViewById(R.id.textoIngles);
            portugues = itemView.findViewById(R.id.textoPortugues);


        }

        @Override
        public void onClick(View view) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        }
    }
}
