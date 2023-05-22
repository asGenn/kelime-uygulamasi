package com.example.kelime_uygulamasi.fragment;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kelime_uygulamasi.databinding.FragmentHomePageBinding;
import com.example.kelime_uygulamasi.repository.ChatGptRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class FragmentHomePage extends Fragment {

    private FragmentHomePageBinding tasarim;
    private ChatGptRepository chatGptRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tasarim = FragmentHomePageBinding.inflate(getLayoutInflater(), container, false);
        chatGptRepository = new ChatGptRepository();
        wordList();
        tasarim.textViewHikaye.setVisibility(View.INVISIBLE);
        tasarim.progressBar.setVisibility(View.INVISIBLE);
        tasarim.buttonHikaye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tasarim.progressBar.setVisibility(View.VISIBLE);
                tasarim.buttonHikaye.setVisibility(View.GONE);
                chatGptRepository.generateWordDetail().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {

                        tasarim.textViewHikaye.setText(s);
                        tasarim.textViewHikaye.setVisibility(View.VISIBLE);

                        tasarim.progressBar.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });

        return tasarim.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void wordList(){
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getUid();

        mFirestore.collection("User").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    List<Map<String, Object>> wordList = (List<Map<String, Object>>) documentSnapshot.get("wordList");

                    if (wordList != null) {
                        StringBuilder kelimeBuilder = new StringBuilder();
                        StringBuilder anlamBuilder = new StringBuilder();

                        for (Map<String, Object> word : wordList) {
                            String kelime = (String) word.get("word");
                            String anlam = (String) word.get("mean");

                            kelimeBuilder.append("- ").append(kelime).append("\n");
                            anlamBuilder.append(anlam).append("\n");
                        }

                        String kelimelerText = kelimeBuilder.toString().trim();
                        String anlamlarText = anlamBuilder.toString().trim();

                        tasarim.textViewKelime.setText(kelimelerText);
                        tasarim.textViewAnlamlar.setText(anlamlarText);


                    } else {
                        Log.d(TAG, "Kullanıcının eklediği kelime yok.");
                    }
                }
            }
        });
    }
}