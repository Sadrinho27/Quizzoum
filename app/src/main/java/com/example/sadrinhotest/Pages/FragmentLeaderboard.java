package com.example.sadrinhotest.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.sadrinhotest.databinding.FragmentLeaderboardBinding;

public class FragmentLeaderboard extends Fragment {

    private FragmentLeaderboardBinding binding;

    public static FragmentLeaderboard newInstance(String param1, String param2) {
        FragmentLeaderboard fragment = new FragmentLeaderboard();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}