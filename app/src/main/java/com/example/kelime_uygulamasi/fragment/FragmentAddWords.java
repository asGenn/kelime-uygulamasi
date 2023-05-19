package com.example.kelime_uygulamasi.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.kelime_uygulamasi.adaptor.myRecyAdaptor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class FragmentAddWords extends Fragment {

    private FragmentAddWordsBinding binding;
    private RecyclerView myRecy;
    private com.example.kelime_uygulamasi.adaptor.myRecyAdaptor myRecyAdaptor;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private ArrayList<Deneme> kuluplers;
    public ArrayList<Deneme> filteredList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddWordsBinding.inflate(getLayoutInflater(), container, false);
        myRecy = (RecyclerView) binding.myRecy;
        kuluplers = new ArrayList<>();
        myRecyAdaptor = new myRecyAdaptor(kuluplers);
        myRecy.setAdapter(myRecyAdaptor);
        myRecy.setLayoutManager(new LinearLayoutManager(getActivity()));
        addPage();
        evenChangeListener();
        myRecyAdaptor.notifyDataSetChanged();

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });


        return binding.getRoot();
    }

    public void filter(String newText){
        ArrayList<Deneme> filteredList = new ArrayList<>();
        for (Deneme item : kuluplers){
            if (item.getWord().toLowerCase().contains(newText.toLowerCase()) || item.getMean().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
            myRecyAdaptor.filterList(filteredList);
        }
    }

    public void addPage() {
        binding.buttonAddPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new FragmentAdd();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.cl, newFragment).commit();
            }
        });
    }

    private void evenChangeListener(){
        mFirestore.collection("Words").orderBy("word", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                kuluplers.add(document.toObject(Deneme.class));
                            }
                            myRecyAdaptor.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(),"Kelimeler Listelenemedi",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}


