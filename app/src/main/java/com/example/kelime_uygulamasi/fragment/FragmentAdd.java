package com.example.kelime_uygulamasi.fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.kelime_uygulamasi.R;
import com.example.kelime_uygulamasi.databinding.FragmentAddBinding;
import com.example.kelime_uygulamasi.repository.Deneme;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FragmentAdd extends Fragment {

    private FragmentAddBinding binding;
    private FirebaseFirestore mFirestore=FirebaseFirestore.getInstance();
    private String kelime,kelimeAnlam;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(getLayoutInflater(), container, false);
        setupOnBackPressed();
        addWord();
        return binding.getRoot();
    }

    private void setupOnBackPressed(){
        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true)  {
            @Override
            public void handleOnBackPressed() {
                Fragment currentFragment = getParentFragmentManager().findFragmentById(R.id.cl);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.remove(currentFragment).commit();
            }
        });
    }

    private void addWord(){
        binding.buttonWordAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kelime = binding.editTextWord.getText().toString();
                kelimeAnlam = binding.editTextWordMean.getText().toString();
                Deneme deneme = new Deneme(kelime,kelimeAnlam);

                //TO-DO uid kısmını düzeltin
                mFirestore.collection("Words")
                        .add(deneme)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    DocumentReference documentReference = task.getResult();
                                    String docId = documentReference.getId();

                                    mFirestore.collection("Words").document(docId)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                        if (documentSnapshot.exists()) {
                                                            String kelime = documentSnapshot.getString("word");
                                                            String kelimeAnlam = documentSnapshot.getString("mean");
                                                            Toast.makeText(getActivity(), "Kelime Eklendi", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        //  Log.d(TAG, "Belge alınamadı: ", task.getException());
                                                    }
                                                }
                                            });
                                } else {
                                    //Log.d(TAG, "Kelime eklenemedi: ", task.getException());
                                }
                            }
                        });
            }
        });
    }
}