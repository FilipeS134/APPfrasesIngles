package com.example.appingles.activity;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button botaoCadastro;
    private Usuario usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome = findViewById(R.id.editText_nome);
        email = findViewById(R.id.editText_emailCadastro);
        senha = findViewById(R.id.editText_senhaCadastro);
        botaoCadastro = findViewById(R.id.button_cadastro);

        botaoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = new Usuario();
                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                if (usuario.getSenha().isEmpty() || usuario.getEmail().isEmpty() || usuario.getNome().isEmpty()){
                    Toast.makeText(CadastroUsuarioActivity.this,"O Campo está vazio, Porfavor tente denovo", Toast.LENGTH_SHORT).show();
                }else{
                    cadastrarUsuario();
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
                    Toast.makeText(CadastroUsuarioActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
                    FirebaseUser usuarioFirebaseUser = task.getResult().getUser();
                    usuario.setId(usuarioFirebaseUser.getUid());
                    usuario.salvar();

                    autenticacao.signOut();
                    finish();
                }else{
                    String erroExecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e) {
                        erroExecao = "Digite uma senha mais forte, contendo mais caracteres!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExecao = "O e-mail digitado é invalido, digite outro e-mail!";

                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExecao = "O e-mail já existe!";
                    } catch (Exception e) {
                        erroExecao = "Ao efetuar o cadastro!";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroUsuarioActivity.this, "Erro: " + erroExecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}