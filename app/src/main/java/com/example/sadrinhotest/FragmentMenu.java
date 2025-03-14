package com.example.sadrinhotest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sadrinhotest.databinding.FragmentMenuBinding;


public class FragmentMenu extends Fragment {

    private FragmentMenuBinding binding;

    private UserViewModel userViewModel;

    public static FragmentMenu newInstance(String param1, String param2) {
        FragmentMenu fragment = new FragmentMenu();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Récupère le ViewModel partagé
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        if (binding != null) {
            binding.PlayBtn.setOnClickListener(v -> {
                FragmentQuiz fragmentQuiz = new FragmentQuiz();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.conteneur, fragmentQuiz)
                        .addToBackStack(null)
                        .commit();
            });

            binding.logoutBtn.setOnClickListener(v -> {
                UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
                userViewModel.setUser(null);

                Log.d("STATE", "Utilisateur déconnecté !");

                FragmentAccueil fragmentAccueil = new FragmentAccueil();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.conteneur, fragmentAccueil)
                        .addToBackStack(null)
                        .commit();
            });

            userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
                if (user != null) {
                    if (user.isAdmin(requireContext())) {
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
                        .replace(R.id.conteneur, fragmentAdmin)
                        .addToBackStack(null)
                        .commit();
            });
        }

    }
}