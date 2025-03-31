package com.example.sadrinhotest.data.models;

import java.util.List;

public class Question {
    private int idQuestion;
    private String libelle;
    private int idTypeDiff;
    private int idTheme;
    private List<Reponse> reponses;

    // Getters et setters
    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getIdTypeDiff() {
        return idTypeDiff;
    }

    public void setIdTypeDiff(int idTypeDiff) {
        this.idTypeDiff = idTypeDiff;
    }

    public int getIdTheme() {
        return idTheme;
    }

    public void setIdTheme(int idTheme) {
        this.idTheme = idTheme;
    }

    public List<Reponse> getReponses() {
        return reponses;
    }

    public void setReponses(List<Reponse> reponses) {
        this.reponses = reponses;
    }
}

