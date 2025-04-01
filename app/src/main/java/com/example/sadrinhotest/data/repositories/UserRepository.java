package com.example.sadrinhotest.data.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sadrinhotest.api.ApiService;
import com.example.sadrinhotest.data.retrofit.RetrofitClient;
import com.example.sadrinhotest.data.models.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserRepository {
    private final ApiService apiService;

    public UserRepository() {
        Retrofit retrofit = RetrofitClient.getInstance();
        this.apiService = retrofit.create(ApiService.class);
    }

    public LiveData<List<User>> getUsers() {
        MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>();

        apiService.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    usersLiveData.setValue(response.body());
                } else {
                    usersLiveData.setValue(Collections.emptyList()); // Aucune donnée trouvée
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Échec de la requête", t);
                usersLiveData.setValue(Collections.emptyList()); // En cas d'échec
            }
        });

        return usersLiveData;
    }

    public LiveData<User> getUserByPseudo(Map<String, String> params) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();

        apiService.getUserByPseudo(params).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userLiveData.setValue(response.body());
                } else {
                    userLiveData.setValue(null); // Aucun utilisateur trouvé
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Échec de la requête", t);
                userLiveData.setValue(null); // En cas d'échec
            }
        });

        return userLiveData;
    }

    public LiveData<Boolean> addUser(User newUser) {
        MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();

        apiService.addUser(newUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    resultLiveData.setValue(true); // Succès
                } else {
                    resultLiveData.setValue(false); // Échec de la mise à jour
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                resultLiveData.setValue(false); // Erreur réseau ou autre
            }
        });

        return resultLiveData;
    }

    public LiveData<Boolean> updateUserRole(String pseudo, int newRole) {
        MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();

        Map<String, String> params = new HashMap<>();
        params.put("pseudo", pseudo);
        params.put("newRole", String.valueOf(newRole));

        apiService.updateUserRole(params).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    resultLiveData.setValue(true); // Succès
                } else {
                    resultLiveData.setValue(false); // Échec de la mise à jour
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                resultLiveData.setValue(false); // Erreur réseau ou autre
            }
        });

        return resultLiveData;
    }

    public LiveData<Boolean> deleteUser(String pseudo) {
        MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();

        Map<String, String> params = new HashMap<>();
        params.put("pseudo", pseudo);

        apiService.deleteUser(params).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    resultLiveData.setValue(true); // Suppression réussie
                } else {
                    resultLiveData.setValue(false); // Échec de la suppression
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                resultLiveData.setValue(false); // Échec de l’appel API
            }
        });

        return resultLiveData;
    }

}
