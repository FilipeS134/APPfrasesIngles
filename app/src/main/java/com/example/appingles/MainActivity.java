package com.example.appingles;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference firebaseDatabaseReferencia = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = firebaseAuth.getInstance();

        // login do usuario
        firebaseAuth.signInWithEmailAndPassword("filipe@hotmail.com", "filip")
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){//sucesso ao cadastrar usuario
                            Log.i("singInUser", "Sucesso ao logar usuario!!");
                        }else{// erro ao cadastrar usuario
                            Log.i("singInUser", "Erro ao logar usuario!!");
                        }

                    }
                });

        // Cadastro de Usuario
        /*firebaseAuth.createUserWithEmailAndPassword("filipe@hotmail.com", "filipe")
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){//sucesso ao cadastrar usuario
                            Log.i("createUser", "Sucesso ao cadastrar usuario!!");
                        }else{// erro ao cadastrar usuario
                            Log.i("createUser", "Erro ao cadastrar usuario!!");
                        }
                    }
                }); */
    }
}
