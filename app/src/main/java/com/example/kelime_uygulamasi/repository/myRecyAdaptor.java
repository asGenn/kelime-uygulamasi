package com.example.kelime_uygulamasi.repository;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelime_uygulamasi.R;

import java.util.ArrayList;
import java.util.HashMap;

public class myRecyAdaptor extends RecyclerView.Adapter<myRecyAdaptor.Myholder> {

    ArrayList<deneme> kelimeler;

    public myRecyAdaptor(ArrayList<deneme> kelimeler) {
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
        holder.textViewWord.setText(kelimeler.get(position).getKelime());
        holder.textViewWordMean.setText(kelimeler.get(position).getKelimeAnlam());
    }

    @Override
    public int getItemCount() {
        return kelimeler.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {

        TextView textViewWord, textViewWordMean;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            textViewWord = itemView.findViewById(R.id.textViewWord);
            textViewWordMean = itemView.findViewById(R.id.textViewWordMean);
        }
    }
}
