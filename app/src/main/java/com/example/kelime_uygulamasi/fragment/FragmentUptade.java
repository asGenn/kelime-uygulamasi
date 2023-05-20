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
import com.example.kelime_uygulamasi.databinding.FragmentUptadeBinding;
import com.example.kelime_uygulamasi.models.Deneme;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FragmentUptade extends Fragment {

    private FragmentUptadeBinding binding;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private HashMap<String, Object> myData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUptadeBinding.inflate(getLayoutInflater(), container, false);
        setupOnBackPressed();
        binding.buttonUptade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateWord(binding.editTextW.getText().toString(),binding.editTextWM.getText().toString());
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
        Deneme  deneme = new Deneme(word,mean);

        mFirestore.collection("Words").document(FirebaseAuth.getInstance().getUid())
                .update(Deneme.convertToMap(deneme))
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Veri Güncellendi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
