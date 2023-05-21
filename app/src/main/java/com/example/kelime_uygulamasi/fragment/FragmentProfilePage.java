package com.example.kelime_uygulamasi.fragment;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kelime_uygulamasi.MainActivity;
import com.example.kelime_uygulamasi.R;
import com.example.kelime_uygulamasi.databinding.FragmentProfilePageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class FragmentProfilePage extends Fragment {

    private FragmentProfilePageBinding tasarim;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tasarim = FragmentProfilePageBinding.inflate(getLayoutInflater(), container, false);
        email();
        logout();
        wordSize();
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

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
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

    public void wordSize(){
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getUid();

        mFirestore.collection("User").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    List<Map<String, Object>> wordList = (List<Map<String, Object>>) documentSnapshot.get("wordList");

                    if (wordList != null) {
                        int wordCount = wordList.size();
                        if (wordCount != 0) {
                            String wordCountText = String.valueOf(wordCount);
                            tasarim.textViewWordSize.setText("Kelime Sayınız = "+wordCountText);
                        } else {
                            tasarim.textViewWordSize.setText("");
                        }
                    } else {
                        Log.d(TAG, "Kullanıcının eklediği kelime yok.");
                    }
                }
            }
        });
    }
}
