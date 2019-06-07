package com.example.appingles.modelo;

import android.content.ContentValues;

import com.google.firebase.database.Exclude;

public class Frases implements Comparable<Frases> {
    @Exclude
    private Integer id;

    private String ingles;
    private String portugues;

    public Frases() {

    }

    public Frases(Integer id, String ingles, String portugues) {
        this.id = id;
        this.ingles = ingles;
        this.portugues = portugues;
    }

    public Frases(String ingles, String portugues) {
        this.ingles = ingles;
        this.portugues = portugues;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIngles() {
        return ingles;
    }

    public void setIngles(String ingles) {
        this.ingles = ingles;
    }

    public String getPortugues() {
        return portugues;
    }

    public void setPortugues(String portugues) {
        this.portugues = portugues;
    }


    @Override
    public int compareTo(Frases frases) {
        return this.ingles.compareTo(frases.getIngles());
    }
}
