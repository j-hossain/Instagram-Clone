package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.instagramclone.databinding.ActivityHomeBinding;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private DatabaseConnector dbConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbConnect = new DatabaseConnector();
        ActivityHomeBinding homeBinding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        homeBinding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbConnect.LogoutUser(new CallBackClass() {
                    @Override
                    public void onFailure(String msg) {

                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(HomeActivity.this, "User Logged Out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                });
            }
        });
        ListView userList = homeBinding.userList;
        ArrayList<String> usernames = new ArrayList<String>();
        dbConnect.getAllUsers();
        usernames.add("Jahin");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,usernames);
        userList.setAdapter(arrayAdapter);
    }
}