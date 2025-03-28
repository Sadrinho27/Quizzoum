package com.example.sadrinhotest.Pages;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.sadrinhotest.Interface.ApiService;
import com.example.sadrinhotest.R;
import com.example.sadrinhotest.RetrofitClient;
import com.example.sadrinhotest.databinding.FragmentAccueilBinding;
import com.example.sadrinhotest.models.User;
import com.example.sadrinhotest.models.UserViewModel;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentAccueil extends Fragment {

    private FragmentAccueilBinding binding;

    public static FragmentAccueil newInstance(String param1, String param2) {
        FragmentAccueil fragment = new FragmentAccueil();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false); // false si l'utilisateur n'est pas connecté
        String registeredPseudo = sharedPreferences.getString("user_pseudo", "");

        Retrofit retrofit = RetrofitClient.getInstance();
        ApiService apiService = retrofit.create(ApiService.class);
        if (isLoggedIn) {
            Map<String, String> params = new HashMap<>();
            params.put("pseudo", registeredPseudo);

            apiService.getUserByPseudo(params).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        User user = response.body();

                        // Sauvegarder l'utilisateur dans le ViewModel pour l'utiliser dans d'autres fragments
                        UserViewModel viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
                        viewModel.setUser(user);

                        // Log et redirection vers le FragmentMenu
                        Log.d("Pré-Login", "L'utilisateur est déjà connecté, passage au FragmentMenu");
                        FragmentMenu fragmentMenu = new FragmentMenu();
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.conteneur, fragmentMenu)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        // Gestion des erreurs si l'utilisateur n'est pas trouvé
                        Log.d("API", "Utilisateur non trouvé");
                        Toast.makeText(getContext(), "Erreur : Utilisateur non trouvé", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    // Erreur de connexion
                    Log.d("API", "Erreur lors de la récupération de l'utilisateur : " + t.getMessage());
                    Toast.makeText(getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.d("Pré-Login", "L'utilisateur n'est pas déjà connecté!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccueilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

                Retrofit retrofit = RetrofitClient.getInstance();
                ApiService apiService = retrofit.create(ApiService.class);

                apiService.getUsers().enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful()) {
                            List<User> users = response.body();
                            boolean userFound = false;

                            for (User user : users) {
                                Log.d("API", "UserAdmin : " + user.toString());
                                if (user.getPseudo().equals(pseudoProvided)) {
                                    userFound = true;
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

                            if (!userFound) {
                                Toast.makeText(getContext(), "Pseudo incorrect", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Erreur de communication avec le serveur", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Log.d("API", "Erreur lors de la récupération des utilisateurs : " + t.getMessage());
                        Toast.makeText(getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
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