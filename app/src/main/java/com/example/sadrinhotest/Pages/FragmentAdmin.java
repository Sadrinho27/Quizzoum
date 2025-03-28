package com.example.sadrinhotest.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.sadrinhotest.R;
import com.example.sadrinhotest.databinding.FragmentAdminBinding;

public class FragmentAdmin extends Fragment {
    private FragmentAdminBinding binding;

    public static FragmentAdmin newInstance(String param1, String param2) {
        FragmentAdmin fragment = new FragmentAdmin();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (binding != null) {

            binding.manageUsersBtn.setOnClickListener(v -> {
                FragmentManageUser fragmentManageUser = new FragmentManageUser();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.conteneur, fragmentManageUser)
                        .addToBackStack(null)
                        .commit();
            });

            binding.backBtn.setOnClickListener(v -> {
                FragmentMenu fragmentMenu = new FragmentMenu();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.conteneur, fragmentMenu)
                        .addToBackStack(null)
                        .commit();
            });
        }
    }
}