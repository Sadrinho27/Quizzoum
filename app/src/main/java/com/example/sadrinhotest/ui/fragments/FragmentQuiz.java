package com.example.sadrinhotest.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sadrinhotest.api.ApiService;
import com.example.sadrinhotest.databinding.FragmentQuizBinding;
import com.example.sadrinhotest.data.models.Question;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class FragmentQuiz extends Fragment {

    private FragmentQuizBinding binding;
    private List<Question> questionsList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private boolean isAnswered = false;
    private ApiService apiService;
    private Call<List<Question>> apiCall;
    private static final String TAG = "FragmentQuiz";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}