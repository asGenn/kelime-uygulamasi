package com.example.kelime_uygulamasi.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kelime_uygulamasi.databinding.FragmentHomePageBinding;
import com.example.kelime_uygulamasi.repository.ChatGptRepository;

public class FragmentHomePage extends Fragment {

    private FragmentHomePageBinding tasarim;
    private ChatGptRepository chatGptRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tasarim = FragmentHomePageBinding.inflate(getLayoutInflater(), container, false);
        chatGptRepository = new ChatGptRepository();
        tasarim.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatGptRepository.generateWordDetail();
                chatGptRepository.generateWordDetail().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        tasarim.textView.setText(s);
                        tasarim.button.setVisibility(View.GONE);
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
}