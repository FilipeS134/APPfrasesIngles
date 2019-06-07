package com.example.appingles.modelo;

import com.example.appingles.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private int imagem;
    private int imagem_capa;

    public Usuario() {

    }

    public void salvar(){
        DatabaseReference referenciaFireBase = ConfiguracaoFirebase.getFirebase();
        referenciaFireBase.child("usuarios").child(getId()).setValue(this);
    }


    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getImagem_capa() {
        return imagem_capa;
    }

    public void setImagem_capa(int imagem_capa) {
        this.imagem_capa = imagem_capa;
    }
}