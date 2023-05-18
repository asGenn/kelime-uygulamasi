package com.example.kelime_uygulamasi.fragment;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.kelime_uygulamasi.R;
import com.example.kelime_uygulamasi.databinding.FragmentAddWordsBinding;
import com.example.kelime_uygulamasi.repository.Deneme;
import com.example.kelime_uygulamasi.repository.myRecyAdaptor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class FragmentAddWords extends Fragment implements com.example.kelime_uygulamasi.repository.uptadePage{

    private FragmentAddWordsBinding binding;
    private RecyclerView myRecy;
    private SearchView searchView;
    private com.example.kelime_uygulamasi.repository.myRecyAdaptor myRecyAdaptor;
    private FirebaseFirestore mFirestore=FirebaseFirestore.getInstance();
    private ArrayList<Deneme> kuluplers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddWordsBinding.inflate(getLayoutInflater(), container, false);

        binding.searchView.clearFocus();
        binding.searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterTest(newText);
                return false;
            }
        });

        myRecy = (RecyclerView) binding.myRecy;
        kuluplers=new ArrayList<>();
        myRecyAdaptor=new myRecyAdaptor(kuluplers, this);
        myRecy.setAdapter(myRecyAdaptor);
        myRecy.setLayoutManager(new LinearLayoutManager(getActivity()));
        wordAdd();
        diziOlustur();
        myRecyAdaptor.notifyDataSetChanged();
        return binding.getRoot();
    }

    public void filterTest(String text){
        ArrayList<Deneme> filteredList = new ArrayList<>();
        for (Deneme item : kuluplers){
            if (item.getWord().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(getActivity(), "Kelime bulunamadı", Toast.LENGTH_SHORT).show();
        }else {
            myRecyAdaptor.setFilteredList(filteredList);
        }
    }

    public void wordAdd(){
        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment2 = new FragmentAdd();
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.cl, fragment2);
                binding.cl.removeAllViews();
                fragmentTransaction.commit();
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

    @Override
    public void onItemClicked(int position) {
        if (position==0){
            Fragment fragment2 = new FragmentUptade();
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.cl, fragment2);
            binding.cl.removeAllViews();
            fragmentTransaction.commit();
        } else if (position==1){
         wordRemove();
        }
    }
}