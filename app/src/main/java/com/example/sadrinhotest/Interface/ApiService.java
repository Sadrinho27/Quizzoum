package com.example.sadrinhotest.Interface;

import com.example.sadrinhotest.models.Question;
import com.example.sadrinhotest.models.Session;
import com.example.sadrinhotest.models.Theme;
import com.example.sadrinhotest.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    // Récupérer tous les utilisateurs
    @GET("api.php?resource=users")
    Call<List<User>> getUsers();

    // Récupérer tous les thèmes
    @GET("api.php?resource=themes")
    Call<List<Theme>> getThemes();

    // Récupérer toutes les questions avec leurs réponses
    @GET("api.php?resource=questions")
    Call<List<Question>> getQuestions();

    // Récupérer toutes les sessions
    @GET("api.php?resource=sessions")
    Call<List<Session>> getSessions();
}
