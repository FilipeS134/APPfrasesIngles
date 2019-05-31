package com.example.appingles.modelo;

public class Frases {
    private int icon;
    private String ingles;
    private String portugues;

    public Frases() {

    }

    public Frases(int icon, String ingles, String portugues) {
        this.icon = icon;
        this.ingles = ingles;
        this.portugues = portugues;
    }

    public Frases( String ingles, String portugues) {
        this.ingles = ingles;
        this.portugues = portugues;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
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
}
