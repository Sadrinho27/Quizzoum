package com.example.sadrinhotest.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sadrinhotest.Adapter.UserAdapter;
import com.example.sadrinhotest.DatabaseHelper;
import com.example.sadrinhotest.R;
import com.example.sadrinhotest.databinding.FragmentManageUserBinding;
import com.example.sadrinhotest.models.User;
import com.example.sadrinhotest.models.UserViewModel;

import java.util.List;

public class FragmentManageUser extends Fragment {

    private FragmentManageUserBinding binding;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private DatabaseHelper dbHelper;
    private List<User> userList;
    private UserViewModel userViewModel;
    private User currentUser;

    public static FragmentManageUser newInstance(String param1, String param2) {
        FragmentManageUser fragment = new FragmentManageUser();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManageUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialiser RecyclerView et DatabaseHelper
        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        dbHelper = new DatabaseHelper(requireContext());

        // Récupérer la liste des utilisateurs
        userList = dbHelper.getAllUsers();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUser = user;
                // Créer un adapter
                userAdapter = new UserAdapter(requireContext(), userList, currentUser);

                // Configurer le RecyclerView
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                recyclerView.setAdapter(userAdapter);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (binding != null) {

            binding.backBtn.setOnClickListener(v -> {
                FragmentAdmin fragmentAdmin = new FragmentAdmin();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.conteneur, fragmentAdmin)
                        .addToBackStack(null)
                        .commit();
            });

        }
    }
}