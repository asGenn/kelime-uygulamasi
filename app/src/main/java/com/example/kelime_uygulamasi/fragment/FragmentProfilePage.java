package com.example.kelime_uygulamasi.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kelime_uygulamasi.R;
import com.example.kelime_uygulamasi.databinding.FragmentProfilePageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentProfilePage extends Fragment {

    private FragmentProfilePageBinding tasarim;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tasarim = FragmentProfilePageBinding.inflate(getLayoutInflater(), container, false);
        email();
        logout();
        return tasarim.getRoot();
    }

    public void logout() {
        mAuth = FirebaseAuth.getInstance();

        if (tasarim!=null){
            tasarim.buttonCikis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.signOut();
                    SharedPreferences preferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isLoggedIn", false);
                    editor.apply();

                    SharedPreferences rememberMePreferences = getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                    SharedPreferences.Editor rememberMeEditor = rememberMePreferences.edit();
                    rememberMeEditor.putString("remember", "false");
                    rememberMeEditor.apply();

                    Toast.makeText(getActivity(), "Hesaptan çıkış yapıldı", Toast.LENGTH_SHORT).show();

                    Fragment newFragment = new FragmentSigninPage();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.pr, newFragment);
                    transaction.commit();
                }
            });
        }

    }

    public void email(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user!=null){
            String username = user.getEmail();
            tasarim.textViewUsername.setText(username);
        } else {
            tasarim.textViewUsername.setText("");
        }
    }

}
