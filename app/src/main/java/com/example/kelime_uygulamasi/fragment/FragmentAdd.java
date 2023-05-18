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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FragmentAdd extends Fragment {

    private FragmentAddBinding binding;
    private FirebaseFirestore mFirestore=FirebaseFirestore.getInstance();
    private HashMap<String,Object> myData;
    private String kelime,kelimeAnlam;

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
                setEnabled(false);
                Fragment fragment2 = new FragmentAddWords();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.lc, fragment2);
                binding.lc.removeAllViews();
                fragmentTransaction.commit();
            }
        });
    }

    private void addWord(){
        binding.buttonWordAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kelime = binding.editTextWord.getText().toString();
                kelimeAnlam = binding.editTextWordMean.getText().toString();
                myData=new HashMap<>();
                myData.put("Words Name:",kelime);
                myData.put("Words Mean:",kelimeAnlam);

                mFirestore.collection("Words").document("Kelimeler")
                        .set(myData)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "Kelime Eklendi", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}