package com.example.kelime_uygulamasi.repository;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class PanelWordAdd {
    private FirebaseFirestore mFirestore= FirebaseFirestore.getInstance();
    private HashMap<String,Object> myData;

    public PanelWordAdd(FirebaseFirestore mFirestore, HashMap<String, Object> myData) {
        this.mFirestore = mFirestore;
        this.myData = myData;
    }

    public FirebaseFirestore getmFirestore() {
        return mFirestore;
    }

    public void setmFirestore(FirebaseFirestore mFirestore) {
        this.mFirestore = mFirestore;
    }

    public HashMap<String, Object> getMyData() {
        return myData;
    }

    public void setMyData(HashMap<String, Object> myData) {
        this.myData = myData;
    }
}
