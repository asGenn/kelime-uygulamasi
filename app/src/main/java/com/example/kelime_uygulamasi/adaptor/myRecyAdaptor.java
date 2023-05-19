package com.example.kelime_uygulamasi.adaptor;

import static android.content.ContentValues.TAG;

import android.content.ClipData;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelime_uygulamasi.R;
import com.example.kelime_uygulamasi.fragment.FragmentAddWords;
import com.example.kelime_uygulamasi.fragment.FragmentUptade;
import com.example.kelime_uygulamasi.repository.Deneme;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class myRecyAdaptor extends RecyclerView.Adapter<myRecyAdaptor.Myholder> {

    ArrayList<Deneme> kelimeler;
    String kelime, kelimeAnlam;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    public ArrayList<Deneme> filteredList;
    public ArrayList<Deneme> dataList;

    public myRecyAdaptor(ArrayList<Deneme> kelimeler) {
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
                kelime = holder.textViewWord.getText().toString();

                mFirestore.collection("Words")
                        .whereEqualTo("word", kelime)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String documentId = document.getId(); //

                                        DocumentReference docRef = mFirestore.collection("Words").document(documentId);

                                        docRef.delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        //FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                                                        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                        //fragmentTransaction.detach(FragmentAddWords.this);
                                                        //fragmentTransaction.attach(FragmentAddWords.this);
                                                        //fragmentTransaction.commit();
                                                        Toast.makeText(v.getContext(), "Kelime silindi", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d(TAG, "Kelime silinemedi: ", e);
                                                    }
                                                });
                                    }
                                } else {
                                    Toast.makeText(v.getContext(), "Kelime ID'si bulunurken hata oluştu", Toast.LENGTH_SHORT).show();
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
