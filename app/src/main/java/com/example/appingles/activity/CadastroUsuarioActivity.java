package com.example.appingles.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appingles.R;
import com.example.appingles.config.ConfiguracaoFirebase;
import com.example.appingles.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class CadastroUsuarioActivity extends AppCompatActivity {

    public EditText nome;
    private EditText email;
    private EditText senha;
    private CircularProgressButton botaoCadastro;
    private Usuario usuario;

    private FirebaseAuth autenticacao;


    private boolean isCampoVazio(String valor){
        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultado;
    }

    private boolean isEmailValido(String email){
        boolean resultado = (isCampoVazio(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        return resultado;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        nome = findViewById(R.id.editText_nome);
        email = findViewById(R.id.editText_emailCadastro);
        senha = findViewById(R.id.editText_senhaCadastro);
        botaoCadastro = findViewById(R.id.button_cadastro);
        usuario = new Usuario();

        botaoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCampoVazio(nome.getText().toString()) || isCampoVazio(senha.getText().toString()) || isEmailValido(email.getText().toString())){
                    Toast.makeText(CadastroUsuarioActivity.this, "O campo está vazio, Porfavor tente de novo", Toast.LENGTH_SHORT).show();
                }else{
                    usuario.setNome(nome.getText().toString());
                    usuario.setEmail(email.getText().toString());
                    usuario.setSenha(senha.getText().toString());
                    cadastrarUsuario();
                    botaoCadastro.startAnimation();
                }

            }
        });

    }


    private void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        usuario.setId(task.getResult().getUser().getUid());
                        usuario.salvar();

                        Toast.makeText(CadastroUsuarioActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();

                        finish();
                    }else{
                        String erroExecao = "";
                        try {
                            throw task.getException();
                        }catch (FirebaseAuthWeakPasswordException e) {
                            botaoCadastro.revertAnimation();
                            erroExecao = "Digite uma senha mais forte, contendo mais caracteres!";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            botaoCadastro.revertAnimation();
                            erroExecao = "O e-mail digitado é invalido, digite outro e-mail!";
                        } catch (FirebaseAuthUserCollisionException e) {
                            botaoCadastro.revertAnimation();
                            erroExecao = "O e-mail já existe!";
                        }catch (Exception e) {
                            botaoCadastro.revertAnimation();
                            e.printStackTrace();
                        }
                        Toast.makeText(CadastroUsuarioActivity.this, "Erro: " + erroExecao, Toast.LENGTH_LONG).show();
                    }
            }
        });
    }
}
