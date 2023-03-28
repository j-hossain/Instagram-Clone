package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.instagramclone.databinding.ActivityFeedBinding;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    private DatabaseConnector dbConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbConnect = new DatabaseConnector();
        ActivityFeedBinding feedBinding = DataBindingUtil.setContentView(this,R.layout.activity_feed);
//        dbConnect.

        String userID = getIntent().getStringExtra("userID").toString();
        dbConnect.getUserImages(userID, new CallBackWithParams() {
            @Override
            public void onSuccess(ArrayList<String> results) {
                for(String image:results){
                    dbConnect.getImage(image, new imageRecieverCallback() {
                        @Override
                        public void onSuccess(byte[] imageByteArray) {
                            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByteArray,0,imageByteArray.length);
                            ImageView imageView = new ImageView(getApplicationContext());
                            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));
                            imageView.setImageBitmap(imageBitmap);
                            feedBinding.linearLayoutMain.addView(imageView);
                        }
                    });
                }
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onSuccess() {

            }
        });


    }
}