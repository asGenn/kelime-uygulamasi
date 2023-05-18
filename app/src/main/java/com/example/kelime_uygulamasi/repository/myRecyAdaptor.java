package com.example.kelime_uygulamasi.repository;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelime_uygulamasi.R;
import com.example.kelime_uygulamasi.fragment.FragmentAdd;

import java.util.ArrayList;
import java.util.List;

public class myRecyAdaptor extends RecyclerView.Adapter<myRecyAdaptor.Myholder> {

    private final uptadePage uptade_page;
    ArrayList<deneme> kelimeler;

    public void setFilteredList(ArrayList<deneme> filteredList){
        this.kelimeler=filteredList;
    }

    public myRecyAdaptor(ArrayList<deneme> kelimeler, uptadePage uptade_page) {
        this.kelimeler=kelimeler;
        this.uptade_page=uptade_page;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.kelime_item,parent,false);
        return new Myholder(view, uptade_page);
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
        Button buttonUptadePage;

        public Myholder(@NonNull View itemView, uptadePage uptade_page) {
            super(itemView);
            textViewWord = itemView.findViewById(R.id.textViewWord);
            textViewWordMean = itemView.findViewById(R.id.textViewWordMean);

            buttonUptadePage = itemView.findViewById(R.id.buttonUptadePage);
            buttonUptadePage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myRecyAdaptor.this.uptade_page != null){
                        int pos =getAdapterPosition();
                        if (pos!=RecyclerView.NO_POSITION){
                            myRecyAdaptor.this.uptade_page.onItemClicked(pos);
                        }
                    }
                }
            });
        }
    }
}
