package com.example.kelime_uygulamasi.repository;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.kelime_uygulamasi.models.Deneme;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseRepository {
    private final FirebaseAuth _auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore _firestore = FirebaseFirestore.getInstance();
    private final MutableLiveData<Deneme> liveData;

    public FirebaseRepository() {
        liveData = new MutableLiveData<>();
    }

    public void signIn(){
        _auth.createUserWithEmailAndPassword("","").addOnCompleteListener(task -> {

        });
    }
    public void getWord(){
        _firestore.collection("Words").document(_auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        Deneme deneme = (Deneme) documentSnapshot.getData();
                        liveData.postValue(deneme);
                    }

                }
            }
        });

    }

    public MutableLiveData<Deneme> getLiveData() {
        return liveData;
    }
}
