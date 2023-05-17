package com.example.kelime_uygulamasi;

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
import android.widget.Toast;

import com.example.kelime_uygulamasi.databinding.FragmentSigninPageBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentSigninPage extends Fragment {

    private FragmentSigninPageBinding tasarim;
    private String txtEmail, txtSifre;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tasarim = FragmentSigninPageBinding.inflate(getLayoutInflater(), container, false);
        mAuth=FirebaseAuth.getInstance();
        passwordVisible();
        signUp();
        signInButton();
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
                Fragment fragment2 = new FragmentSignupPage();
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.layout, fragment2);
                tasarim.layout.removeAllViews();
                fragmentTransaction.commit();
            }
        });
    }

    public void signInButton(){
        tasarim.buttonGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    public void signIn(){
        txtEmail=tasarim.editTextEmail.getText().toString();
        txtSifre=tasarim.editTextPassword.getText().toString();

        if(!TextUtils.isEmpty(txtEmail)&& !TextUtils.isEmpty(txtSifre)){
            mAuth.signInWithEmailAndPassword(txtEmail,txtSifre)
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            mUser=mAuth.getCurrentUser();
                            FragmentHomePage myFragment = new FragmentHomePage();
                            getChildFragmentManager().beginTransaction()
                                    .replace(R.id.layout, myFragment)
                                    .commit();
                            tasarim.layout.removeAllViews();
                        }
                    }).addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Kullanıcı Adı veya Şifre Yanlış", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else
            Toast.makeText(getActivity(), "Email ve Şifre Boş Olamaz", Toast.LENGTH_SHORT).show();
    }
}