package com.example.sadrinhotest.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sadrinhotest.R;
import com.example.sadrinhotest.adapters.QuestionAdapter;
import com.example.sadrinhotest.api.ApiService;
import com.example.sadrinhotest.data.models.Question;
import com.example.sadrinhotest.data.models.Reponse;
import com.example.sadrinhotest.data.models.User;
import com.example.sadrinhotest.data.retrofit.RetrofitClient;
import com.example.sadrinhotest.databinding.FragmentQuizBinding;
import com.example.sadrinhotest.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentQuiz extends Fragment {

    private FragmentQuizBinding binding;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private final static String TAG = "FragmentQuiz";
    private UserViewModel userViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        fetchQuestions();

        if (binding != null) {
            binding.nextBtn.setOnClickListener(v -> nextQuestion());
        }
    }

    private void fetchQuestions() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        apiService.getQuestions().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Question>> call, @NonNull Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questions = response.body();

                    Collections.shuffle(questions);

                    if (questions.size() > 10) {
                        questions = questions.subList(0, 10);
                    }

                    displayQuestions();
                } else {
                    Toast.makeText(getContext(), "Erreur lors de la récupération des questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Question>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayQuestions() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            binding.question.setText(currentQuestion.getLibelle());

            binding.difficultyText.setText(String.format("Difficulté: %s", currentQuestion.getDifficulty()));
            binding.themeText.setText(String.format("Thème: %s", currentQuestion.getTheme()));

            List<Reponse> shuffledAnswers = currentQuestion.getReponses();
            Collections.shuffle(shuffledAnswers);

            QuestionAdapter adapter = new QuestionAdapter(shuffledAnswers, this::checkAnswer);
            binding.answersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.answersRecyclerView.setAdapter(adapter);

            binding.resultContainer.setVisibility(View.GONE);
            binding.nextBtn.setEnabled(false);
        }
    }

    private void checkAnswer(Reponse answer) {
        if (answer.isCorrect()) {
            binding.resultText.setText("Bonne réponse !");
            score++;
        } else {
            binding.resultText.setText("Mauvaise réponse !");
        }

        binding.resultContainer.setVisibility(View.VISIBLE);
        binding.nextBtn.setEnabled(true);

        disableAllAnswers();
    }

    private void disableAllAnswers() {
        RecyclerView.Adapter adapter = binding.answersRecyclerView.getAdapter();
        if (adapter != null) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                Button answerButton = (Button) binding.answersRecyclerView.getLayoutManager().findViewByPosition(i).findViewById(R.id.answerButton);
                if (answerButton != null) {
                    answerButton.setEnabled(false);
                }
            }
        }
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            displayQuestions();
        } else {
            binding.nextBtn.setVisibility(View.INVISIBLE);
            saveSession();

            FragmentQuizResult fragmentQuizResult = new FragmentQuizResult(score);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragmentQuizResult)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void saveSession() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        User player = userViewModel.getUser().getValue();

        if (player == null) {
            Toast.makeText(getContext(), "Utilisateur non trouvé", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("score", String.valueOf(score));
        params.put("idUser", String.valueOf(player.getId()));

        apiService.addSession(params).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int sessionId = response.body();  // ➜ Ici, on récupère l'ID de session
                    Log.d("SAVE_SESSION", "Session enregistrée avec ID: " + sessionId);

                    // Maintenant, on sauvegarde les questions associées à cette session
                    saveSessionQuestions(sessionId);
                    saveUserScore(player);
                } else {
                    Toast.makeText(getContext(), "Erreur lors de l'enregistrement de la session", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Code : " + response.code() + ", Message : " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
                Log.e("NETWORK_ERROR", "Échec de l'appel API", t);
            }
        });
    }

    private void saveSessionQuestions(int sessionId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        Map<String, Object> params = new HashMap<>();
        params.put("idSession", sessionId);

        List<Integer> questionIds = new ArrayList<>();
        for (Question question : questions) {
            questionIds.add(question.getId());
        }
        params.put("questions", questionIds);

        apiService.addSessionQuestion(params).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("SAVE_SESSION_QUESTIONS", "Questions associées à la session enregistrées.");
                } else {
                    Log.e("API_ERROR", "Code : " + response.code() + ", Message : " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
                Log.e("NETWORK_ERROR", "Échec de l'appel API", t);
            }
        });
    }

    private void saveUserScore(User player) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        // Préparer les paramètres pour l'appel API
        Map<String, String> params = new HashMap<>();
        params.put("pseudo", player.getPseudo());
        params.put("score", String.valueOf(score));

        apiService.updateUserScore(params).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("SAVE_USER_SCORE", "Score mis à jour avec succès pour l'utilisateur : " + player.getPseudo());
                } else {
                    Log.e("API_ERROR", "Erreur lors de la mise à jour du score. Code : " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("NETWORK_ERROR", "Échec de l'appel API pour la mise à jour du score", t);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}