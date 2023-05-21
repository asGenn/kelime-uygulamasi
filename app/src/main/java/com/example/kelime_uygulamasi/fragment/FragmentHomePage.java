package com.example.kelime_uygulamasi.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kelime_uygulamasi.databinding.FragmentHomePageBinding;
import com.example.kelime_uygulamasi.repository.ChatGptRepository;

public class FragmentHomePage extends Fragment {

    private FragmentHomePageBinding tasarim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tasarim = FragmentHomePageBinding.inflate(getLayoutInflater(), container, false);

        ChatGptRepository chatGptRepository = new ChatGptRepository();
        chatGptRepository.getChatComplation();
        return tasarim.getRoot();
    }
}