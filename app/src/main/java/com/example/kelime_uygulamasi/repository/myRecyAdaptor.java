package com.example.kelime_uygulamasi.repository;

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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelime_uygulamasi.R;
import com.example.kelime_uygulamasi.fragment.FragmentAdd;
import com.example.kelime_uygulamasi.fragment.FragmentUptade;

import java.util.ArrayList;
import java.util.List;

public class myRecyAdaptor extends RecyclerView.Adapter<myRecyAdaptor.Myholder> {

    ArrayList<Deneme> kelimeler;

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
                FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.cl);
                if (currentFragment != null) {
                    fragmentManager.beginTransaction().remove(currentFragment).commit();
                }
                Fragment newFragment = new FragmentUptade();
                fragmentManager.beginTransaction().add(R.id.cl, newFragment).addToBackStack(null).commit();
            }
        });

        holder.buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // REMOVE
            }
        });
    }

    @Override
    public int getItemCount() {
        return kelimeler.size();
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
