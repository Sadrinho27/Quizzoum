package com.example.sadrinhotest.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sadrinhotest.R;
import com.example.sadrinhotest.api.ApiService;
import com.example.sadrinhotest.data.models.Question;
import com.example.sadrinhotest.data.models.Reponse;
import com.example.sadrinhotest.data.retrofit.RetrofitClient;
import com.example.sadrinhotest.ui.fragments.FragmentAccueil;
import com.example.sadrinhotest.databinding.ActivityMainBinding;
import com.example.sadrinhotest.data.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ApiService apiService;
    private Call<List<Question>> apiCall;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        Retrofit retrofit = RetrofitClient.getInstance();
        apiService = retrofit.create(ApiService.class);
        fetchData();

        // Ajouter le fragment seulement si savedInstanceState est null
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new FragmentAccueil())
                    .commit();
        }
    }

    private void fetchData() {
        apiCall = apiService.getQuestions();
        apiCall.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful()) {
                    List<Question> datas = response.body();
                    if (datas != null && !datas.isEmpty()) {
                        for (Question data : datas) {
                            Log.d(TAG, "Question: " + data.getLibelle() + " Difficulty : "  + data.getDifficulty() + " Theme : "  + data.getTheme());
                            for (Reponse data1 : data.getReponses()) {
                                Log.d(TAG, "Reponse: " + data1.getLibelle() + " Is Correct : "  + (data1.isCorrect() ? "Yes" : "No"));
                            }

                        }
                    } else {
                        Log.d(TAG, "Aucune données trouvée");
                    }
                } else {
                    Log.d(TAG, "Erreur de récupération des données: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
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

