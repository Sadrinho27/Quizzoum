package com.example.sadrinhotest.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sadrinhotest.R;
import com.example.sadrinhotest.adapters.LeaderboardAdapter;
import com.example.sadrinhotest.api.ApiService;
import com.example.sadrinhotest.data.retrofit.RetrofitClient;
import com.example.sadrinhotest.databinding.FragmentLeaderboardBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLeaderboard extends Fragment {

    private FragmentLeaderboardBinding binding;
    private LeaderboardAdapter leaderboardAdapter;
    private final List<Map<String, Object>> leaderboardList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);

        // Initialiser le RecyclerView
        leaderboardAdapter = new LeaderboardAdapter(leaderboardList);
        binding.recyclerViewLeaderboard.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewLeaderboard.setAdapter(leaderboardAdapter);

        // Charger les données du leaderboard depuis l'API
        loadLeaderboardData();

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
            binding.backBtn.setOnClickListener(v -> {
                FragmentMenu fragmentMenu = new FragmentMenu();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragmentMenu)
                        .addToBackStack(null)
                        .commit();
            });
        }
    }

    private void loadLeaderboardData() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getLeaderboard().enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(@NonNull Call<List<Map<String, Object>>> call, @NonNull Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    leaderboardList.clear();
                    leaderboardList.addAll(response.body());
                    leaderboardAdapter.notifyDataSetChanged();
                } else {
                    Log.e("Leaderboard", "Erreur lors de la récupération des données.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Map<String, Object>>> call, @NonNull Throwable t) {
                Log.e("Leaderboard", "Erreur réseau: " + t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
