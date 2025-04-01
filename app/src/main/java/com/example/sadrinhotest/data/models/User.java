package com.example.sadrinhotest.data.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("idUser")
    private int idUser;
    @SerializedName("pseudo")
    private String pseudo;
    @SerializedName("password")
    private String password;
    @SerializedName("isAdmin")
    private int isAdmin;

    public User(String pseudo, String password, int isAdmin) {
        this.pseudo = pseudo;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    // Getter et setter pour idUser
    public int getId() {
        return idUser;
    }
    public void setId(int idUser) {
        this.idUser = idUser;
    }
    // Getter et setter pour pseudo
    public String getPseudo() {
        return pseudo;
    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    // Getter et setter pour password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter et setter pour isAdmin
    public boolean isAdmin() { // Convertit l’int en boolean
        return isAdmin == 1;
    }
    public void setAdmin(boolean admin) { // Convertit le boolean en int avant de l'affecter
        this.isAdmin = admin ? 1 : 0;
    }
    public int getIsAdminInt() {
        return isAdmin;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{pseudo='" + pseudo + "', password='" + password + "', isAdmin=" + isAdmin + "}";
    }
}
