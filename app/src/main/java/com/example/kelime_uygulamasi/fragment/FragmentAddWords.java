package com.example.kelime_uygulamasi.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kelime_uygulamasi.R;
import com.example.kelime_uygulamasi.databinding.FragmentAddWordsBinding;
import com.example.kelime_uygulamasi.repository.deneme;
import com.example.kelime_uygulamasi.repository.myRecyAdaptor;

import java.util.ArrayList;

public class FragmentAddWords extends Fragment {

    private FragmentAddWordsBinding binding;
    private RecyclerView myRecy;
    private ArrayList<deneme> kuluplers;
    private com.example.kelime_uygulamasi.repository.myRecyAdaptor myRecyAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddWordsBinding.inflate(getLayoutInflater(), container, false);
        myRecy = (RecyclerView) binding.myRecy;
        kuluplers=new ArrayList<>();
        myRecyAdaptor=new myRecyAdaptor(kuluplers);
        myRecy.setAdapter(myRecyAdaptor);
        myRecy.setLayoutManager(new LinearLayoutManager(getActivity()));
        diziolustur();
        wordAdd();
        myRecyAdaptor.notifyDataSetChanged();
        return binding.getRoot();
    }

    public void diziolustur() {
        kuluplers.add(new deneme("Jesus","FENERBAHÇE"));
        kuluplers.add(new deneme("Okan","GALATASARAY"));
        kuluplers.add(new deneme("Şenol","BEŞİKTAŞ"));
        kuluplers.add(new deneme("Jesus","FENERBAHÇE"));
        kuluplers.add(new deneme("Okan","GALATASARAY"));
        kuluplers.add(new deneme("Şenol","BEŞİKTAŞ"));
        kuluplers.add(new deneme("Jesus","FENERBAHÇE"));
        kuluplers.add(new deneme("Okan","GALATASARAY"));
        kuluplers.add(new deneme("Şenol","BEŞİKTAŞ"));
        kuluplers.add(new deneme("Jesus","FENERBAHÇE"));
        kuluplers.add(new deneme("Okan","GALATASARAY"));
        kuluplers.add(new deneme("Şenol","BEŞİKTAŞ"));
    }

    public void wordAdd(){
        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment2 = new FragmentEkle();
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.cl, fragment2);
                binding.cl.removeAllViews();
                fragmentTransaction.commit();
            }
        });
    }
}