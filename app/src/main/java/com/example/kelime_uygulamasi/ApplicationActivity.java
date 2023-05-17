package com.example.kelime_uygulamasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.kelime_uygulamasi.databinding.ActivityApplicationBinding;

public class ApplicationActivity extends AppCompatActivity {

    private ActivityApplicationBinding tasarim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tasarim = ActivityApplicationBinding.inflate(getLayoutInflater());
        setContentView(tasarim.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        NavigationUI.setupWithNavController(tasarim.bottomNav, navHostFragment.getNavController());
    }
}