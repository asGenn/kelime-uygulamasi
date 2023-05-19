package com.example.kelime_uygulamasi.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import com.example.kelime_uygulamasi.R;
import com.example.kelime_uygulamasi.databinding.FragmentAddWordsBinding;
import com.example.kelime_uygulamasi.repository.Deneme;
import com.example.kelime_uygulamasi.repository.myRecyAdaptor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class FragmentAddWords extends Fragment {

    private FragmentAddWordsBinding binding;
    private RecyclerView myRecy;
    private com.example.kelime_uygulamasi.repository.myRecyAdaptor myRecyAdaptor;
    private FirebaseFirestore mFirestore=FirebaseFirestore.getInstance();
    private ArrayList<Deneme> kuluplers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddWordsBinding.inflate(getLayoutInflater(), container, false);
        myRecy = (RecyclerView) binding.myRecy;
        kuluplers=new ArrayList<>();
        myRecyAdaptor=new myRecyAdaptor(kuluplers);
        myRecy.setAdapter(myRecyAdaptor);
        myRecy.setLayoutManager(new LinearLayoutManager(getActivity()));
        addPage();
        diziOlustur();
        myRecyAdaptor.notifyDataSetChanged();
        return binding.getRoot();
    }

    public void addPage(){
        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new FragmentAdd();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.cl, newFragment);
                transaction.commit();
            }
        });
    }

    public void diziOlustur(){
        kuluplers.add(new Deneme("Jesus","FENERBAHÇE"));
        kuluplers.add(new Deneme("Okan","GALATASARAY"));
        kuluplers.add(new Deneme("Şenol","BEŞİKTAŞ"));
        kuluplers.add(new Deneme("Jesus","FENERBAHÇE"));
        kuluplers.add(new Deneme("Okan","GALATASARAY"));
        kuluplers.add(new Deneme("Şenol","BEŞİKTAŞ"));
        kuluplers.add(new Deneme("Jesus","FENERBAHÇE"));
        kuluplers.add(new Deneme("Okan","GALATASARAY"));
        kuluplers.add(new Deneme("Şenol","BEŞİKTAŞ"));
        kuluplers.add(new Deneme("Jesus","FENERBAHÇE"));
        kuluplers.add(new Deneme("Okan","GALATASARAY"));
        kuluplers.add(new Deneme("Şenol","BEŞİKTAŞ"));
    }

    private  void wordRemove(){
        mFirestore.collection("Words").document("Kelimeler")
                .delete()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Kelime basariyla silindi", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}