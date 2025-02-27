package com.example.sadrinhotest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sadrinhotest.databinding.FragmentAccueilBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAccueil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAccueil extends Fragment {

    private FragmentAccueilBinding binding;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragmentAccueil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAccueil.
     */
    public static FragmentAccueil newInstance(String param1, String param2) {
        FragmentAccueil fragment = new FragmentAccueil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Utiliser le binding pour bien attacher le layout du fragment
        binding = FragmentAccueilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Vérifie que binding est bien initialisé avant d'accéder aux vues
        if (binding != null) {

            String inputValue  = String.valueOf(binding.inputName.getText());

            binding.button.setOnClickListener(v -> {
                Log.d("STATE", "Vous avez cliqué sur le bouton");
                Log.d("STATE", String.valueOf(binding.inputName.getText()));

                binding.textResult.setText(String.valueOf(binding.inputName.getText()));
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Nettoyer le binding pour éviter les memory leaks
    }

}