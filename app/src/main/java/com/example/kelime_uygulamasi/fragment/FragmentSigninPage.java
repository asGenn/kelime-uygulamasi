package com.example.kelime_uygulamasi.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.kelime_uygulamasi.ApplicationActivity;
import com.example.kelime_uygulamasi.R;
import com.example.kelime_uygulamasi.databinding.FragmentSigninPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentSigninPage extends Fragment {

    private FragmentSigninPageBinding tasarim;
    public String txtEmail, txtSifre;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private boolean isRememberMeChecked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tasarim = FragmentSigninPageBinding.inflate(getLayoutInflater(), container, false);
        mAuth=FirebaseAuth.getInstance();
        passwordVisible();
        signUp();
        signInButton();
        rememberMe();
        runRememberMe();
        return tasarim.getRoot();
    }

    public void passwordVisible(){
        tasarim.passwordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tasarim.editTextPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    tasarim.editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    tasarim.passwordVisibility.setBackgroundResource(R.drawable.baseline_visibility_off_24);
                }else {
                    tasarim.editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    tasarim.passwordVisibility.setBackgroundResource(R.drawable.baseline_visibility_24);
                }
            }
        });
    }

    public void signUp(){
        tasarim.textViewKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new FragmentSignupPage();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.layout, newFragment).commit();
            }
        });
    }

    public void signInButton(){
        tasarim.buttonGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtEmail = tasarim.editTextEmail.getText().toString();
                txtSifre = tasarim.editTextPassword.getText().toString();
                signIn(txtEmail, txtSifre);
            }
        });
    }

    public void signIn(String txtEmail, String txtSifre){
        txtEmail = tasarim.editTextEmail.getText().toString();
        txtSifre = tasarim.editTextPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(txtEmail, txtSifre)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String userEmail = user.getEmail();
                                SharedPreferences preferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.apply();

                                Intent intent = new Intent(getActivity(), ApplicationActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Hatalı kullanıcı adı veya şifre", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void rememberMe() {
        txtEmail = tasarim.editTextEmail.getText().toString();
        txtSifre = tasarim.editTextPassword.getText().toString();

        tasarim.checkBoxRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                if (isChecked) {
                    editor.putString("remember", "true");
                    editor.putString("email", txtEmail);
                    editor.putString("password", txtSifre);
                    isRememberMeChecked = true;
                    Toast.makeText(getActivity(), "Seçildi", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("remember", "false");
                    editor.remove("email");
                    editor.remove("password");
                    isRememberMeChecked = false;
                    Toast.makeText(getActivity(), "Seçilmedi", Toast.LENGTH_SHORT).show();
                }
                editor.apply();
            }
        });
    }

    public void runRememberMe() {
        SharedPreferences preferences = getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
        String checkbox = preferences.getString("remember", "");

        if (checkbox.equals("true")) {
            if (isLoggedIn()) {
                Intent intent = new Intent(getActivity(), ApplicationActivity.class);
                startActivity(intent);
            } else if (isRememberMeChecked) {
                String savedEmail = preferences.getString("email", "");
                String savedPassword = preferences.getString("password", "");

                if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
                    signIn(savedEmail, savedPassword);
                } else {
                    Toast.makeText(getActivity(), "Lütfen Giriş Yapınız", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Lütfen Giriş Yapınız", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Lütfen Giriş Yapınız", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isLoggedIn() {
        SharedPreferences preferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
        return preferences.getBoolean("isLoggedIn", false);
    }





}