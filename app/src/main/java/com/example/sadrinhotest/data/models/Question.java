package com.example.sadrinhotest.data.models;

import java.util.List;

public class Question {
    private int idQuestion;
    private String libelle;
    private String difficulty;
    private String theme;
    private List<Reponse> reponses;

    // Getters et setters

    public int getId() {
        return idQuestion;
    }

    public void setId(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTheme() {
        return theme;
    }
    public void setTheme(String theme) {
        this.theme = theme;
    }

    public List<Reponse> getReponses() {
        return reponses;
    }

    public void setReponses(List<Reponse> reponses) {
        this.reponses = reponses;
    }
}

