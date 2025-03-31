package com.example.sadrinhotest.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sadrinhotest.data.models.User;
import com.example.sadrinhotest.data.repositories.UserRepository;

import java.util.List;
import java.util.Map;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>();
    private final UserRepository userRepository;

    public UserViewModel() {
        userRepository = new UserRepository();
    }

    // Setter et Getter pour l'utilisateur connecté
    public void setUser(User user) {
        userLiveData.setValue(user);
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    // Récupérer la liste des utilisateurs depuis l'API
    public LiveData<List<User>> getUsers() {
        return userRepository.getUsers();
    }

    public LiveData<User> getUserByPseudo(Map<String, String> params) {
        return userRepository.getUserByPseudo(params);
    }

    public LiveData<Boolean> updateUserRole(String pseudo, int newRole) {
        return userRepository.updateUserRole(pseudo, newRole);
    }

    public LiveData<Boolean> deleteUser(String pseudo) {
        return userRepository.deleteUser(pseudo);
    }
}


