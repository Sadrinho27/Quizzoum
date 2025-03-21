package com.example.sadrinhotest;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sadrinhotest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.conteneur, new FragmentAccueil())
                    .commit();
        }
    }
}
