package com.example.kelime_uygulamasi.models;

import java.util.HashMap;
import java.util.Map;

public class Deneme {
    private String word,mean;


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public Deneme(String word, String mean) {
        this.word = word;
        this.mean = mean;
    }
    static public Map<String,Object> convertToMap(Deneme deneme){
        Map<String,Object> map = new HashMap<>();
        map.put("word",deneme.word);
        map.put("mean",deneme.mean);
        return map;
    }
}
