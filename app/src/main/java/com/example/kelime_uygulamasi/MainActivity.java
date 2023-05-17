package com.example.kelime_uygulamasi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.kelime_uygulamasi.databinding.ActivityMainBinding;
import com.example.kelime_uygulamasi.fragment.FragmentSigninPage;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding tasarim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tasarim = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(tasarim.getRoot());
        fragment();
    }

    public void fragment(){
        FragmentSigninPage myFragment = new FragmentSigninPage();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout, myFragment)
                .commit();
        tasarim.layout.removeAllViews();
    }
}