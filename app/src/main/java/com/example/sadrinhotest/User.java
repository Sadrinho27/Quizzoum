package com.example.sadrinhotest;

public class User {
    private String pseudo;
    private String password;
    private boolean isAdmin;

    public User(String pseudo, String password) {
        this.pseudo = pseudo;
    }

    // Constructeurs, getters et setters

    public String getPseudo() {
        return this.pseudo;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
