package com.example.instagramclone;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseConnector {
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    public DatabaseConnector(){
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("database","Database connected");
    }
    public Model_userInfo checkLoggedinUser(){
        if(mAuth.getCurrentUser()==null) return null;
        return new Model_userInfo(mAuth.getCurrentUser().getUid());
    }

    public void LoginUser(String email, String password, CallBackClass callBack){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    callBack.onFailure("The email or the password is not correct");
                }
                else{
                    callBack.onSuccess();
                }
            }
        });
    }

    public void RegisterUser(String email, String password, CallBackClass callBack){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    callBack.onFailure(task.getException().getMessage().toString());
                }
                else{

                    callBack.onSuccess();
                }
            }
        });
    }
}
