package com.example.kelime_uygulamasi.repository;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseRepository {
    private final FirebaseAuth _auth = FirebaseAuth.getInstance();
    public void signIn(){
        _auth.createUserWithEmailAndPassword("","").addOnCompleteListener(task -> {

        });
    }

}
