package com.example.sadrinhotest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sadrinhotest.databinding.FragmentAdminBinding;
import com.example.sadrinhotest.databinding.FragmentMenuBinding;

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
}