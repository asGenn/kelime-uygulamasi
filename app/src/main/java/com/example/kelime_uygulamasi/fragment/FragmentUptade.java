package com.example.kelime_uygulamasi.fragment;

import android.content.Context;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class FragmentUptade extends Fragment {

    private FragmentUptadeBinding binding;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private HashMap<String, Object> myData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUptadeBinding.inflate(getLayoutInflater(), container, false);
        setupOnBackPressed();
        //updateWord(myData, "Kelimeler");
        return binding.getRoot();
    }

    private void setupOnBackPressed() {
        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setEnabled(false);
                Fragment fragment2 = new FragmentAddWords();
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.lcu, fragment2);
                binding.lcu.removeAllViews();
                fragmentTransaction.commit();
            }
        });
    }

    private void updateWord(HashMap<String, Object> myData, final String Kelimeler) {
        mFirestore.collection("Words").document("Kelimeler")
                .update(myData)
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
