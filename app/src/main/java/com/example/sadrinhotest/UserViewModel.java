package com.example.sadrinhotest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private MutableLiveData<User> userLiveData = new MutableLiveData<User>();

    public void setUser(User user) {
        userLiveData.setValue(user);
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }
}

