package com.example.sadrinhotest.ui.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sadrinhotest.R;
import com.example.sadrinhotest.databinding.FragmentQuizResultBinding;

public class FragmentQuizResult extends Fragment {
    private FragmentQuizResultBinding binding;
    private int score;

    public FragmentQuizResult(int score) {
        this.score = score;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = com.example.sadrinhotest.databinding.FragmentQuizResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {  // true signifie que le callback est actif
                    @Override
                    public void handleOnBackPressed() {
                        // Ne rien faire pour désactiver le bouton retour
                    }
                });

        binding.scoreText.setText(String.format("Votre score : %s/10", score));

        binding.retryButton.setOnClickListener(v -> {
            FragmentQuiz fragmentQuiz = new FragmentQuiz();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragmentQuiz)
                    .addToBackStack(null)
                    .commit();
        });

        binding.homeButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new FragmentMenu())
                    .commit();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}