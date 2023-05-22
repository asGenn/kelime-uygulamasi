package com.example.kelime_uygulamasi.fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kelime_uygulamasi.R;
import com.example.kelime_uygulamasi.databinding.FragmentAddBinding;
import com.example.kelime_uygulamasi.models.Deneme;
import com.example.kelime_uygulamasi.repository.WordList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentAdd extends Fragment {

    private FragmentAddBinding binding;
    private FirebaseFirestore mFirestore=FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(getLayoutInflater(), container, false);
        setupOnBackPressed();
        addWord();
        return binding.getRoot();
    }

    private void setupOnBackPressed(){
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

    private void addWord(){
        ArrayList<Deneme> wordListGlobal = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        binding.buttonWordAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFirestore.collection("User").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            List<Map<String,Object>> wordList = (List<Map<String, Object>>) documentSnapshot.get("wordList");
                            ArrayList<Deneme> wordList1 = new ArrayList<>();
                            if(wordList !=null){
                                for(Map<String,Object> wordData : wordList){
                                    Deneme deneme = new Deneme((String) wordData.get("word"), (String) wordData.get("mean"));
                                    wordList1.add(deneme);

                                }

                            }
                            wordListGlobal.clear();
                            wordList1.add(new Deneme(binding.editTextWord.getText().toString(),binding.editTextWordMean.getText().toString()));
                            binding.editTextWord.setText("");
                            binding.editTextWordMean.setText("");
                            wordListGlobal.addAll(wordList1);
                            mFirestore.collection("User").document(mAuth.getUid()).set(new WordList(wordListGlobal)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(getActivity().getApplicationContext(),"Eklendi",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });

            }
        });
    }

}