package com.example.sadrinhotest.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.sadrinhotest.R;
import com.example.sadrinhotest.databinding.FragmentMenuBinding;
import com.example.sadrinhotest.viewmodels.UserViewModel;


public class FragmentMenu extends Fragment {
    private FragmentMenuBinding binding;
    private UserViewModel userViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
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

        if (binding != null) {
            binding.PlayBtn.setOnClickListener(v -> {
                FragmentQuiz fragmentQuiz = new FragmentQuiz();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragmentQuiz)
                        .addToBackStack(null)
                        .commit();
            });

            binding.LeaderboardBtn.setOnClickListener(v -> {
                FragmentLeaderboard fragmentLeaderboard = new FragmentLeaderboard();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragmentLeaderboard)
                        .addToBackStack(null)
                        .commit();
            });

            binding.logoutBtn.setOnClickListener(v -> {
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("is_logged_in");
                editor.remove("user_pseudo");
                editor.apply();

                userViewModel.setUser(null);

                FragmentAccueil fragmentAccueil = new FragmentAccueil();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragmentAccueil)
                        .addToBackStack(null)
                        .commit();
            });

            userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
                if (user != null) {
                    binding.loginText.setText(String.format("Connecté en tant que : %s", user.getPseudo()));
                    if (user.isAdmin()) {
                        binding.adminBtn.setVisibility(View.VISIBLE);
                    } else {
                        binding.adminBtn.setVisibility(View.INVISIBLE);
                    }
                }
            });

            binding.adminBtn.setOnClickListener(v -> {
                FragmentAdmin fragmentAdmin = new FragmentAdmin();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragmentAdmin)
                        .addToBackStack(null)
                        .commit();
            });
        }
    }

}