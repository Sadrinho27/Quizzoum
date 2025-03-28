package com.example.sadrinhotest.models;

import android.content.Context;

import com.example.sadrinhotest.DatabaseHelper;

public class User {
    private String pseudo;
    private String password;
    private boolean isAdmin;

    // Constructeurs, getters et setters
    public User(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public boolean isAdmin(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        return dbHelper.checkIfUserIsAdmin(this.pseudo);
    }

}
