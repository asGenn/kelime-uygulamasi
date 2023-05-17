package com.example.kelime_uygulamasi.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kelime_uygulamasi.databinding.FragmentHomePageBinding;

public class FragmentHomePage extends Fragment {

    private FragmentHomePageBinding tasarim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tasarim = FragmentHomePageBinding.inflate(getLayoutInflater(), container, false);
        return tasarim.getRoot();
    }
}