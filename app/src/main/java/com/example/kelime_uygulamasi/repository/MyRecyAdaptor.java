package com.example.kelime_uygulamasi.repository;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kelime_uygulamasi.R;
import com.example.kelime_uygulamasi.fragment.FragmentUptade;
import com.example.kelime_uygulamasi.models.Deneme;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyRecyAdaptor extends RecyclerView.Adapter<MyRecyAdaptor.Myholder> {

    ArrayList<Deneme> kelimeler;
    String kelime, kelimeAnlam;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    public ArrayList<Deneme> filteredList;
    public ArrayList<Deneme> dataList;
    private FirebaseAuth mAuth;

    public MyRecyAdaptor(ArrayList<Deneme> kelimeler) {
        this.kelimeler=kelimeler;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.kelime_item,parent,false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        holder.textViewWord.setText(kelimeler.get(position).getWord());
        holder.textViewWordMean.setText(kelimeler.get(position).getMean());
        holder.buttonUptadePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kelime = holder.textViewWord.getText().toString();
                kelimeAnlam = holder.textViewWordMean.getText().toString();
                FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.cl);
                if (currentFragment != null) {
                    fragmentManager.beginTransaction().remove(currentFragment).commit();
                }
                Fragment newFragment = new FragmentUptade();
                Bundle bundle = new Bundle();
                bundle.putString("kelime", kelime);
                bundle.putString("kelimeAnlami", kelimeAnlam);
                newFragment.setArguments(bundle);
                fragmentManager.beginTransaction().add(R.id.cl, newFragment).addToBackStack(null).commit();
            }
        });

        holder.buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                kelime = holder.textViewWord.getText().toString();

                mFirestore.collection("User").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            List<Map<String, Object>> wordList = (List<Map<String, Object>>) documentSnapshot.get("wordList");
                            ArrayList<Deneme> updatedWordList = new ArrayList<>();

                            if (wordList != null) {
                                for (Map<String, Object> wordData : wordList) {
                                    Deneme deneme = new Deneme((String) wordData.get("word"), (String) wordData.get("mean"));
                                    updatedWordList.add(deneme);
                                }
                            }

                            String kelimeToRemove = holder.textViewWord.getText().toString(); // Silinecek kelimeyi al

                            for (Deneme word : updatedWordList) {
                                if (word.getWord().equals(kelimeToRemove)) { // Silinecek kelimeyi bulduk
                                    updatedWordList.remove(word); // Kelimeyi listeden kaldır
                                    break;
                                }
                            }

                            mFirestore.collection("User").document(mAuth.getUid()).update("wordList", updatedWordList)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(v.getContext(), "Kelime Silindi", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.e(TAG, "Kelime silme hatası: " + task.getException().getMessage());
                                                Toast.makeText(v.getContext(), "Kelime Silme İşlemi Başarısız", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return kelimeler.size();
    }


    public void setData(ArrayList<Deneme> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<Deneme> filteredList){
        kelimeler = filteredList;
        notifyDataSetChanged();
    }

    public class Myholder extends RecyclerView.ViewHolder {

        TextView textViewWord, textViewWordMean;
        Button buttonUptadePage, buttonRemove;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            textViewWord = itemView.findViewById(R.id.textViewWord);
            textViewWordMean = itemView.findViewById(R.id.textViewWordMean);

            buttonUptadePage = itemView.findViewById(R.id.buttonUptadePage);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }
}
