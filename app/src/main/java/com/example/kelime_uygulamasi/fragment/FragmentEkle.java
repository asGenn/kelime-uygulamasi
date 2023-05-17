package com.example.kelime_uygulamasi.fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kelime_uygulamasi.R;
import com.example.kelime_uygulamasi.databinding.FragmentAddWordsBinding;
import com.example.kelime_uygulamasi.databinding.FragmentEkleBinding;

public class FragmentEkle extends Fragment {

    private FragmentEkleBinding tasarim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tasarim = FragmentEkleBinding.inflate(getLayoutInflater(), container, false);
        setupOnBackPressed();
        return tasarim.getRoot();
    }

    private void setupOnBackPressed(){
        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setEnabled(false);
                Fragment fragment2 = new FragmentAddWords();
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.lc, fragment2);
                tasarim.lc.removeAllViews();
                fragmentTransaction.commit();
            }
        });
    }
}