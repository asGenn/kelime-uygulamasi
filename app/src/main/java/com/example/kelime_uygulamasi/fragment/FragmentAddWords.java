package com.example.kelime_uygulamasi.fragment;



import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import com.example.kelime_uygulamasi.R;
import com.example.kelime_uygulamasi.databinding.FragmentAddWordsBinding;
import com.example.kelime_uygulamasi.models.Deneme;

import com.example.kelime_uygulamasi.repository.MyRecyAdaptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FragmentAddWords extends Fragment {

    private FragmentAddWordsBinding binding;
    private RecyclerView myRecy;
    private MyRecyAdaptor myRecyAdaptor;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private ArrayList<Deneme> kuluplers;
    public ArrayList<Deneme> filteredList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddWordsBinding.inflate(getLayoutInflater(), container, false);
        myRecy = (RecyclerView) binding.myRecy;
        kuluplers = new ArrayList<>();
        myRecyAdaptor = new MyRecyAdaptor(kuluplers);
        myRecy.setAdapter(myRecyAdaptor);
        myRecy.setLayoutManager(new LinearLayoutManager(getActivity()));
        addPage();
        eventChangeListener();



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
        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new FragmentAdd();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.cl, newFragment).commit();
            }
        });
    }


    private void eventChangeListener() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid(); // Kullanıcının kimliği

        mFirestore.collection("User").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Hata durumuyla başa çıkın
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {

                    List<Map<String,Object>> wordList = (List<Map<String, Object>>) documentSnapshot.get("wordList");
                    ArrayList<Deneme> wordList1 = new ArrayList<>();
                    if(wordList !=null){

                        for(Map<String,Object> wordData : wordList){
                            Deneme deneme = new Deneme((String) wordData.get("word"), (String) wordData.get("mean"));
                            wordList1.add(deneme);
                        }
                    }

                    kuluplers.clear();
                    kuluplers.addAll(wordList1);
                    myRecyAdaptor.notifyDataSetChanged();
                }
            }
        });
    }



}


