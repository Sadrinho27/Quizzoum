package com.example.sadrinhotest.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sadrinhotest.R;
import com.example.sadrinhotest.adapters.QuestionAdapter;
import com.example.sadrinhotest.api.ApiService;
import com.example.sadrinhotest.data.models.Question;
import com.example.sadrinhotest.data.models.Reponse;
import com.example.sadrinhotest.data.retrofit.RetrofitClient;
import com.example.sadrinhotest.databinding.FragmentQuizBinding;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentQuiz extends Fragment {

    private FragmentQuizBinding binding;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        updateScore();

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

    private void updateScore() {
        binding.scoreText.setText(String.format(Locale.getDefault(), "Score: %d",score));
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            displayQuestions();
        } else {
            binding.nextBtn.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(), "Quiz terminé !", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

