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
import com.example.kelime_uygulamasi.models.Deneme;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.cl);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
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
                mFirestore.collection("Words").document(FirebaseAuth.getInstance().getUid())
                        .set(deneme)
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