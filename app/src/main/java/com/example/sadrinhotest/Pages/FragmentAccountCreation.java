package com.example.sadrinhotest.Pages;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.sadrinhotest.Interface.ApiService;
import com.example.sadrinhotest.R;
import com.example.sadrinhotest.RetrofitClient;
import com.example.sadrinhotest.databinding.FragmentAccountCreationBinding;
import com.example.sadrinhotest.models.User;
import com.example.sadrinhotest.models.UserViewModel;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentAccountCreation extends Fragment {

    private FragmentAccountCreationBinding binding;

    public static FragmentAccountCreation newInstance(String param1, String param2) {
        FragmentAccountCreation fragment = new FragmentAccountCreation();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountCreationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.button.setOnClickListener(v -> {
            String pseudo = binding.pseudoInput.getText().toString();
            String password = binding.passwordInput.getText().toString();

            // Vérifier que les champs ne sont pas vides
            if (TextUtils.isEmpty(pseudo)) {
                Toast.makeText(getContext(), "Le pseudo est requis", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Le mot de passe est requis", Toast.LENGTH_SHORT).show();
            } else {
                Retrofit retrofit = RetrofitClient.getInstance();
                ApiService apiService = retrofit.create(ApiService.class);

                // Vérifier si le pseudo existe déjà
                apiService.getUsers().enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful()) {
                            // Vérifier si le pseudo est déjà utilisé
                            List<User> users = response.body();
                            boolean userExists = false;
                            for (User user : users) {
                                if (user.getPseudo().equals(pseudo)) {
                                    userExists = true;
                                    break;
                                }
                            }

                            if (userExists) {
                                Toast.makeText(getContext(), "Le pseudo est déjà pris", Toast.LENGTH_SHORT).show();
                            } else {
                                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                                User newUser = new User(pseudo, hashedPassword, 0);

                                UserViewModel viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
                                viewModel.setUser(newUser);

                                apiService.addUser(newUser).enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if (response.isSuccessful()) {
                                            Log.d("API", "Utilisateur créé avec succès !");

                                            // L'utilisateur est bien créé, donc on le connecte
                                            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean("is_logged_in", true);
                                            editor.putString("user_pseudo", pseudo);
                                            editor.apply();

                                            FragmentMenu fragmentMenu = new FragmentMenu();
                                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                            fragmentManager.beginTransaction()
                                                    .replace(R.id.conteneur, fragmentMenu)
                                                    .addToBackStack(null)
                                                    .commit();
                                        } else {
                                            try {
                                                // Vérifier si l'erreur est retournée par le serveur
                                                if (response.code() == 400) {
                                                    // Extraire le message d'erreur et afficher un Toast
                                                    String errorMessage = response.errorBody().string();
                                                    JSONObject jsonObject = new JSONObject(errorMessage);
                                                    String error = jsonObject.getString("error");

                                                    // Afficher un Toast avec le message d'erreur
                                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // Gérer d'autres codes d'erreur
                                                    Toast.makeText(getContext(), "Une erreur est survenue. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(getContext(), "Une erreur inattendue est survenue", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Log.d("API", "Erreur lors de la création de l'utilisateur : " + t.getMessage());
                                        Toast.makeText(getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
                                    }
                                });
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
            }
        });

    }
}
