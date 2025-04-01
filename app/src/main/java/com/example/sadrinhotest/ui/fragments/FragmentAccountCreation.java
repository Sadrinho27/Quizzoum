package com.example.sadrinhotest.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.sadrinhotest.api.ApiService;
import com.example.sadrinhotest.R;
import com.example.sadrinhotest.data.retrofit.RetrofitClient;
import com.example.sadrinhotest.databinding.FragmentAccountCreationBinding;
import com.example.sadrinhotest.data.models.User;
import com.example.sadrinhotest.viewmodels.UserViewModel;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentAccountCreation extends Fragment {

    private FragmentAccountCreationBinding binding;
    private UserViewModel userViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountCreationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        binding.button.setOnClickListener(v -> {
            String pseudo = binding.pseudoInput.getText().toString();
            String password = binding.passwordInput.getText().toString();

            // Vérifier que les champs ne sont pas vides
            if (TextUtils.isEmpty(pseudo)) {
                Toast.makeText(getContext(), "Le pseudo est requis", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Le mot de passe est requis", Toast.LENGTH_SHORT).show();
            } else {
                // Vérifier si le pseudo existe déjà
                userViewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
                    if (users != null) {
                        boolean pseudoExist = false;
                        for (User user : users) {
                            if (user.getPseudo().equals(pseudo)) {
                                pseudoExist = true;
                                break; // Pas besoin de continuer à vérifier
                            }
                        }

                        if (pseudoExist) {
                            Toast.makeText(getContext(), "Le pseudo est déjà pris", Toast.LENGTH_SHORT).show();
                        } else {
                            // Créer un nouvel utilisateur
                            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                            User newUser = new User(pseudo, hashedPassword, 0);

                            // Ajouter l'utilisateur via ViewModel
                            userViewModel.addUser(newUser).observe(getViewLifecycleOwner(), createdUser -> {
                                Log.d("API", "Utilisateur créé avec succès !");

                                // Connexion automatique
                                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("is_logged_in", true);
                                editor.putString("user_pseudo", pseudo);
                                editor.apply();

                                userViewModel.setUser(newUser);

                                // Passer au menu principal
                                FragmentMenu fragmentMenu = new FragmentMenu();
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.container, fragmentMenu)
                                        .addToBackStack(null)
                                        .commit();
                            });
                        }
                    }
                });
            }
        });


    }
}
