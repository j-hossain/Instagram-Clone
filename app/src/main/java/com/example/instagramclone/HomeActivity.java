package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.instagramclone.databinding.ActivityHomeBinding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private DatabaseConnector dbConnect;

    public void getPhoto(){
        Intent photoGetterIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoGetterIntent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImage = data.getData();
        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            try {
                Bitmap imageBintmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBintmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] imageByteArray = stream.toByteArray();
                dbConnect.uploadImage(imageByteArray, new CallBackClass() {
                    @Override
                    public void onFailure(String msg) {

                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(HomeActivity.this, "Photo Uploaded", Toast.LENGTH_SHORT).show();
                        Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(homeIntent);
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getPhoto();
            }
        }
    }

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
        homeBinding.floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }
                else{
                    getPhoto();
                }
            }
        });

        ListView userList = homeBinding.userList;
        ArrayList<String> usernames = new ArrayList<String>();
        ArrayList<Model_userInfo> userInfoList = new ArrayList<Model_userInfo>();
        dbConnect.getAllUsers(new UserListCallback() {
            @Override
            public void onFailure(String msg) {

            }
            @Override
            public void onSuccess() {

            }
            @Override
            public void setUserList(ArrayList<Model_userInfo> userData) {

                for(Model_userInfo userInfo:userData){
                    userInfoList.add(userInfo);
                    usernames.add(userInfo.getUsername());
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,usernames);
                    userList.setAdapter(arrayAdapter);
                }
            }
        });

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent feedIntent = new Intent(getApplicationContext(), FeedActivity.class);
                feedIntent.putExtra("userID",userInfoList.get(i).getUserID());
                startActivity(feedIntent);
            }
        });
    }
}