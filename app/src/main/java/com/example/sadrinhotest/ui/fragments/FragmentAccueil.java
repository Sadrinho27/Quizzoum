package com.example.sadrinhotest.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.sadrinhotest.R;
import com.example.sadrinhotest.databinding.FragmentAccueilBinding;
import com.example.sadrinhotest.data.models.User;
import com.example.sadrinhotest.viewmodels.UserViewModel;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class FragmentAccueil extends Fragment {

    private FragmentAccueilBinding binding;
    private UserViewModel userViewModel;
    private static final String TAG = "FragmentAccueil";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);
        String registeredPseudo = sharedPreferences.getString("user_pseudo", "");

        if (isLoggedIn) {
            Log.d(TAG, "L'utilisateur est déjà connecté!");
            Map<String, String> params = new HashMap<>();
            params.put("pseudo", registeredPseudo);

            userViewModel.getUserByPseudo(params).observe(this, user -> {
                if (user != null) {
                        userViewModel.setUser(user);
                        FragmentMenu fragmentMenu = new FragmentMenu();
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.conteneur, fragmentMenu)
                                .addToBackStack(null)
                                .commit();
                } else {
                    Toast.makeText(getContext(), "Erreur : Utilisateur non trouvé", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.d(TAG, "L'utilisateur n'est pas déjà connecté!");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccueilBinding.inflate(inflater, container, false);
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
            binding.loginBtn.setEnabled(false);

            binding.createAccountBtn.setOnClickListener(v -> {
                FragmentAccountCreation fragmentAccountCreation = new FragmentAccountCreation();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.conteneur, fragmentAccountCreation)
                        .addToBackStack(null)
                        .commit();
            });

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    checkFields(); // Vérifie si les champs sont remplis
                }

                private void checkFields() {
                    String pseudo = binding.pseudoInput.getText().toString().trim();
                    String password = binding.passwordInput.getText().toString().trim();
                    binding.loginBtn.setEnabled(!pseudo.isEmpty() && !password.isEmpty());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            };

            binding.pseudoInput.addTextChangedListener(textWatcher);
            binding.passwordInput.addTextChangedListener(textWatcher);

            binding.loginBtn.setOnClickListener(v -> {
                String pseudoProvided = binding.pseudoInput.getText().toString();
                String passwordProvided = binding.passwordInput.getText().toString();

                userViewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
                    if (users != null && !users.isEmpty()) {
                        for (User user : users) {
                            if (user.getPseudo().equals(pseudoProvided)) {
                                String hashedPassword = user.getPassword();

                                if (BCrypt.checkpw(passwordProvided, hashedPassword)) {
                                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("is_logged_in", true);
                                    editor.putString("user_pseudo", user.getPseudo());
                                    editor.apply();

                                    UserViewModel viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
                                    viewModel.setUser(user);

                                    FragmentMenu fragmentMenu = new FragmentMenu();
                                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.conteneur, fragmentMenu)
                                            .addToBackStack(null)
                                            .commit();
                                } else {
                                    Toast.makeText(getContext(), "Mot de passe incorrect", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                        }
                    }
                });
            });


        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}