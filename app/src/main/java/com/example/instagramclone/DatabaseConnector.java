package com.example.instagramclone;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseConnector {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    public DatabaseConnector(){
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        Log.i("database","Database connected");
    }
    public UserInfo checkLoggedinUser(){
        if(mAuth.getCurrentUser()==null) return null;
        return new UserInfo(mAuth.getCurrentUser().getUid());
    }
}
