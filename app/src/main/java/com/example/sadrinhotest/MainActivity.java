package com.example.sadrinhotest;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sadrinhotest.Interface.ApiService;
import com.example.sadrinhotest.Pages.FragmentAccueil;
import com.example.sadrinhotest.databinding.ActivityMainBinding;
import com.example.sadrinhotest.models.Question;
import com.example.sadrinhotest.models.Reponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Retrofit retrofit;
    private ApiService apiService;
    private Call<List<Question>> apiCall;  // Stocker la requête pour annulation
    private static final String BASE_URL = "http://192.168.1.15/quizzoum/";
    private static final String TAG = "MainActivity";  // Tag pour Log.d()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        // Retrofit retrofit = RetrofitClient.getInstance();
        // apiService = retrofit.create(ApiService.class);
        // fetchQuestionData();

        // Ajouter le fragment seulement si savedInstanceState est null
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.conteneur, new FragmentAccueil())
                    .commit();
        }
    }

    private void fetchQuestionData() {
        // Appel de l'API pour récupérer les questions
        apiCall = apiService.getQuestions(); // Assigner l'appel API à apiCall
        apiCall.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful()) {
                    // Récupération des questions
                    List<Question> questions = response.body();
                    if (questions != null && !questions.isEmpty()) {
                        // Traitez les données des questions ici
                        for (Question question : questions) {
                            Log.d(TAG, "Question: " + question.getLibelle());

                            // Affichage des réponses
                            List<Reponse> reponses = question.getReponses();
                            for (Reponse reponse : reponses) {
                                Log.d(TAG, "Réponse: " + reponse.getLibelle() + " - Correct: " + (reponse.isCorrect() ? "Oui" : "Non"));
                            }
                        }
                    } else {
                        // Aucun résultat trouvé
                        Log.d(TAG, "Aucune question trouvée");
                    }
                } else {
                    // Gérer l'erreur de réponse
                    Log.d(TAG, "Erreur de récupération des données: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                // Gérer l'échec de la requête
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

        binding = null; // Nettoyage pour éviter les memory leaks
    }

}

