package com.example.kelime_uygulamasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.kelime_uygulamasi.databinding.ActivityMainBinding;
import com.example.kelime_uygulamasi.fragment.FragmentSigninPage;
import com.example.kelime_uygulamasi.fragment.FragmentUptade;
import com.example.kelime_uygulamasi.repository.FirebaseRepository;


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
        Fragment newFragment = new FragmentSigninPage();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.layout);
        if (currentFragment != null){
            fragmentManager.beginTransaction().remove(currentFragment).commit();
        }
        fragmentManager.beginTransaction().add(R.id.layout, newFragment).commit();
    }

}