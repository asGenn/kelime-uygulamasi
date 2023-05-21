package com.example.kelime_uygulamasi.fragment;

import static android.content.ContentValues.TAG;

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

import com.example.kelime_uygulamasi.R;
import com.example.kelime_uygulamasi.databinding.FragmentUptadeBinding;
import com.example.kelime_uygulamasi.models.Deneme;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FragmentUptade extends Fragment {

    private FragmentUptadeBinding binding;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private String word, mean;
    private String kelime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUptadeBinding.inflate(getLayoutInflater(), container, false);
        word = getArguments().getString("kelime");
        mean = getArguments().getString("kelimeAnlami");
        binding.editTextW.setText(word);
        binding.editTextWM.setText(mean);
        setupOnBackPressed();

        binding.buttonUptade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateWord(binding.editTextW.getText().toString(), binding.editTextWM.getText().toString());
            }
        });

        return binding.getRoot();
    }

    private void setupOnBackPressed() {
        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.cl);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(currentFragment).commit();
            }
        });
    }

    private void updateWord(String word,String mean) {
        Deneme deneme = new Deneme(word, mean);
        kelime = getArguments().getString("kelime");

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("word", deneme.getWord());
        updateData.put("mean", deneme.getMean());

        mFirestore.collection("Words")
                .whereEqualTo("word", kelime)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String docId = document.getId();
                                DocumentReference updatedDocRef = mFirestore.collection("Words").document(docId);
                                updatedDocRef.update(updateData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity(), "Kelime Güncellendi", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "Kelime güncelleme hatası: " + e.getMessage());
                                                Toast.makeText(getActivity(), "Kelime Güncelleme İşlemi Başarısız", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Belge alınamadı: " + task.getException());
                        }
                    }
                });
    }

}

