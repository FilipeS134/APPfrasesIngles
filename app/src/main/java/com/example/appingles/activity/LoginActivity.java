package com.example.appingles.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appingles.R;
import com.example.appingles.config.ConfiguracaoFirebase;
import com.example.appingles.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        email = findViewById(R.id.editText_emailLogin);
        senha = findViewById(R.id.editText_senhaLogin);
        botaoLogar = findViewById(R.id.button_login);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = new Usuario();
                usuario.setEmail( email.getText().toString());
                usuario.setSenha( senha.getText().toString());
                if (usuario.getSenha().isEmpty() || usuario.getEmail().isEmpty()){
                    Toast.makeText(LoginActivity.this,"O Campo está vazio", Toast.LENGTH_SHORT).show();
                }else{
                    validarLogin();
                }

            }
        });
    }

    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    public void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if ( task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_LONG).show();
                        abrirTelaPrincipal();
                    }else{
                    String erroExecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e) {
                        erroExecao = "Usuario não existe, ou e-mail errado";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExecao = "Senha errada!!";
                    } catch (Exception e) {
                        erroExecao = "Erro ao efetuar o login";
                        e.printStackTrace(); }
                    Toast.makeText(LoginActivity.this, erroExecao, Toast.LENGTH_LONG).show();
                    }


            }
        });
    }

    public void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }
}
