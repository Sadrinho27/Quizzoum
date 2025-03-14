package com.example.sadrinhotest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sadrinhotest.databinding.FragmentAccueilBinding;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAccueil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAccueil extends Fragment {

    private FragmentAccueilBinding binding;

    public FragmentAccueil() {
        // Required empty public constructor
    }

    public static FragmentAccueil newInstance(String param1, String param2) {
        FragmentAccueil fragment = new FragmentAccueil();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            binding.button.setEnabled(false);

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    checkFields(); // Vérifie si les champs sont remplis
                }

                private void checkFields() {
                    String pseudo = binding.pseudoInput.getText().toString().trim();
                    String password = binding.passwordInput.getText().toString().trim();
                    binding.button.setEnabled(!pseudo.isEmpty() && !password.isEmpty());
                }

                @Override
                public void afterTextChanged(Editable editable) {}
            };

            // Appliquer le TextWatcher aux deux champs
            binding.pseudoInput.addTextChangedListener(textWatcher);
            binding.passwordInput.addTextChangedListener(textWatcher);

            DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
            // dbHelper.deleteDB();

            // Ajouter un utilisateur
            dbHelper.createUser("admin", "admin", true);
            dbHelper.createUser("test", "test", false);
            // dbHelper.deleteUser("test");

            // Récupérer les utilisateurs et les afficher
            // List<String> users = dbHelper.getAllUsers();
            // for (String user : users) {
            //     Log.d("SQLite", "Utilisateur : " + user);
            // }

            binding.button.setOnClickListener(v -> {
                String pseudoProvided = binding.pseudoInput.getText().toString();
                String passwordProvided = binding.passwordInput.getText().toString();

                if (dbHelper.checkIfUserExist(pseudoProvided, passwordProvided)) {
                    Log.d("SQLite", "Connexion réussie pour " + pseudoProvided + " !");

                    User user = new User(pseudoProvided);

                    UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
                    userViewModel.setUser(user);

                    FragmentMenu fragmentMenu = new FragmentMenu();
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.conteneur, fragmentMenu)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Log.e("SQLite", "Pseudo ou mot de passe incorrect !");
                    Toast.makeText(requireContext(), "Pseudo ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}