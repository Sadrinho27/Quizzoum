package com.example.sadrinhotest.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sadrinhotest.R;
import com.example.sadrinhotest.api.ApiService;
import com.example.sadrinhotest.ui.fragments.FragmentAccueil;
import com.example.sadrinhotest.databinding.ActivityMainBinding;
import com.example.sadrinhotest.data.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ApiService apiService;
    private Call<List<User>> apiCall;
    private static final String BASE_URL = "http://192.168.1.15/quizzoum/";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

//        Retrofit retrofit = RetrofitClient.getInstance();
//        apiService = retrofit.create(ApiService.class);
//        fetchData();

        // Ajouter le fragment seulement si savedInstanceState est null
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.conteneur, new FragmentAccueil())
                    .commit();
        }
    }

    private void fetchData() {
        apiCall = apiService.getUsers();
        apiCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> datas = response.body();
                    if (datas != null && !datas.isEmpty()) {
                        Log.d(TAG, datas.toString());
                        for (User data : datas) {
                            Log.d(TAG, "Pseudo: " + data.getPseudo() + " - Hashed Password: " + data.getPassword() + " - Is admin: " + (data.isAdmin() ? "Yes" : "No"));
                        }
                    } else {
                        Log.d(TAG, "Aucune données trouvée");
                    }
                } else {
                    Log.d(TAG, "Erreur de récupération des données: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, "Erreur réseau ou serveur: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Annuler la requête si elle est en cours pour éviter les fuites de mémoire
        if (apiCall != null && !apiCall.isCanceled()) {
            apiCall.cancel();
            Log.d(TAG, "Requête annulée dans onDestroy");
        }

        binding = null;
    }

}

