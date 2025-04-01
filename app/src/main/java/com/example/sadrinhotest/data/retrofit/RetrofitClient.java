package com.example.sadrinhotest.data.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // URL de base de ton API
    private static final String BASE_URL = "http://192.168.1.15/quizzoum/";

    // Instance Retrofit
    private static Retrofit retrofit;

    // Méthode pour récupérer l'instance Retrofit
    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Base URL de l'API
                    .addConverterFactory(GsonConverterFactory.create()) // Convertisseur JSON -> Objet Java
                    .build();
        }
        return retrofit;
    }
}

