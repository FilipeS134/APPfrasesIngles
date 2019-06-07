package com.example.appingles.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appingles.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class PerfilFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;

    StorageReference storageReference;
    String storagePath = "Usuarios_Perfil_Capa_Imgs/";

    private ImageView imagenPerfil, capa_perfil;
    private TextView nome,email;
    private FloatingActionButton floatingActionButton;

    ProgressDialog pd;
    private static  final int CAMERA_REQUEST_CODE = 100;
    private static  final int STORAGE_REQUEST_CODE = 200;
    private static  final int IMAGEM_FOTO_GALERIA_CODE = 300;
    private static  final int IMAGEM_FOTO_CAMERA_CODE = 400;
    String cameraPermissoes[];
    String storagePermissoes[];

    // pegar URI imagen
    Uri imagem_uri;
    String perfilOuCapaFoto;

    public PerfilFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        storageReference = getInstance().getReference();

        //iniciar progress dialogo
        pd = new ProgressDialog(getActivity());

        //iniciar arrays
        cameraPermissoes = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissoes = new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE};

        imagenPerfil = view.findViewById(R.id.fotoperfil);
        capa_perfil = view.findViewById(R.id.capa_imagen);
        nome = view.findViewById(R.id.nome_perfil);
        email = view.findViewById(R.id.email_perfil);
        floatingActionButton = view.findViewById(R.id.action_perfil);


        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String nomes = ""+ ds.child("nome").getValue();
                    String emails = ""+ ds.child("email").getValue();
                    String imagem = ""+ ds.child("imagem").getValue();
                    String capa = ""+ ds.child("capa").getValue();

                    nome.setText(nomes);
                    email.setText(emails);
                    try {
                        Picasso.get().load(imagem).into(imagenPerfil);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.ic_imagem_padrao).into(imagenPerfil);
                    }

                    try {
                        Picasso.get().load(capa).into(capa_perfil);
                    }catch (Exception e){
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarEditarPerfil();
            }
        });

        return view;
    }

    private boolean checarStoragePermissao(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermissao(){
        requestPermissions( storagePermissoes, STORAGE_REQUEST_CODE);
    }

    private boolean checarCameraPermissao(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermissao(){
        requestPermissions( cameraPermissoes, CAMERA_REQUEST_CODE);
    }

    private void mostrarEditarPerfil() {
        String opçoes[] = {"Editar Foto Perfil", "Editar Foto Capa", "Editar Nome"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Selecione Uma Ação");
        builder.setItems(opçoes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    pd.setMessage("Atualiando Foto Perfil");
                    pd.show();
                    perfilOuCapaFoto = "imagem";
                    showImagePicDialog();
                }else if (i == 1){
                    pd.setMessage("Atualizando Foto Capa");
                    pd.show();
                    perfilOuCapaFoto = "capa";
                    showImagePicDialog();
                }else if (i == 2){
                    pd.setMessage("Atualizando Nome Perfil");
                    pd.show();
                    showNomeUpdateDialog("nome");
                }
            }
        });

        builder.create().show();
    }

    private void showNomeUpdateDialog(final String key) {
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        builder.setTitle("Atualizar "+ key);
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        final EditText editText = new EditText(getActivity());
        editText.setHint("Digite seu "+key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);
        builder.setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);

                    databaseReference.child(user.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Atualizado...", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(getActivity(), "Digite seu "+key, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    pd.dismiss();
            }
        });

        builder.create().show();
    }

    private void showImagePicDialog() {
        String opçoes[] = {"Camera", "Galeria"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Selecione");
        builder.setItems(opçoes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    if (!checarCameraPermissao()){
                        requestCameraPermissao();
                    }else {
                        tirarFotoCamera();
                    }
                }else if (i == 1){
                    if (!checarStoragePermissao()){
                        requestStoragePermissao();
                    }else {
                        pegarFotoGaleria();
                    }
                }
            }
        });

        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length > 0){
                    boolean cameraAceitar = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAceitar = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAceitar && writeStorageAceitar){
                        tirarFotoCamera();
                    }else {
                        Toast.makeText(getActivity(), "Porfavor permita acesso a Camera",  Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length > 0){
                    boolean writeStorageAceitar = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAceitar){
                        pegarFotoGaleria();
                    }else {
                        Toast.makeText(getActivity(), "Porfavor permita acesso a Galeria",  Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGEM_FOTO_GALERIA_CODE){
                imagem_uri = data.getData();
                uploadFotoPerfilCapa(imagem_uri);
            }
            if (requestCode == IMAGEM_FOTO_CAMERA_CODE){
                uploadFotoPerfilCapa(imagem_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFotoPerfilCapa(final Uri uri) {
        String filePathAndName = storagePath+ ""+ perfilOuCapaFoto + "_"+ user.getUid();
        StorageReference storageReference1 = storageReference.child(filePathAndName);
        storageReference1.putFile(uri)
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();
                if (uriTask.isSuccessful()){
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(perfilOuCapaFoto, downloadUri.toString());
                    databaseReference.child(user.getUid()).updateChildren(result)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Imagem Updated...", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Erro ao updating Imagem...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Ocorreu um erro", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void pegarFotoGaleria() {
        Intent galeriaIntent = new Intent(Intent.ACTION_PICK);
        galeriaIntent.setType("image/*");
        startActivityForResult(galeriaIntent, IMAGEM_FOTO_GALERIA_CODE);
    }

    private void tirarFotoCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");

        imagem_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagem_uri);
        startActivityForResult(cameraIntent, IMAGEM_FOTO_CAMERA_CODE);
    }


}
