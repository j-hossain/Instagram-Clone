package com.example.instagramclone;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConnector {
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;

    private StorageReference stRef;
    public DatabaseConnector(){
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        stRef = FirebaseStorage.getInstance().getReference();
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

    public void RegisterUser(String username, String email, String password, CallBackClass callBack){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    callBack.onFailure(task.getException().getMessage().toString());
                }
                else{
                    Map<String, Object> user= new HashMap<>();
                    user.put("username",username);
                    user.put("email",email);
                    user.put("id",mAuth.getCurrentUser().getUid().toString());

                    database.collection("Users").document(mAuth.getCurrentUser().getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            callBack.onSuccess();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callBack.onFailure(e.getMessage());
                        }
                    });
                }
            }
        });
    }

    public void LogoutUser(CallBackClass callBack){
        mAuth.signOut();
        callBack.onSuccess();
    }

    public void getAllUsers(UserListCallback userListCallback){
        database.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.isSuccessful()){
                    userListCallback.onFailure(task.getException().getMessage());
                }
                else{
                    ArrayList<Model_userInfo> userList = new ArrayList<>();
                    for(QueryDocumentSnapshot user: task.getResult()){
                        Map userMap = (Map) user.getData();
                        if(userMap.get("id").toString().matches(mAuth.getCurrentUser().getUid().toString())) continue;
                        userList.add(new Model_userInfo(userMap));
                    }
                    userListCallback.setUserList(userList);
                }
            }
        });
    }

    public void uploadImage(byte[] imageByteArray,CallBackClass callBack){
        String userID = mAuth.getCurrentUser().getUid();
        String FileName = Long.toString(System.currentTimeMillis());
        UploadTask upload = stRef.child("images/"+FileName).putBytes(imageByteArray);
        upload.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onFailure(e.getMessage());
            }
        });
        upload.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String postID = Long.toString(System.currentTimeMillis());
                Map<String, Object> post= new HashMap<>();
                post.put("userID",mAuth.getCurrentUser().getUid());
                post.put("image",FileName);
                database.collection("posts").document(postID).set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callBack.onSuccess();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callBack.onFailure(e.getMessage());
                    }
                });
            }
        });
//        database.child("Images").setValue()
    }

    public void getUserImages(String userID, CallBackWithParams callBackWithParams){
        database.collection("posts").whereEqualTo("userID",userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<String> images = new ArrayList<>();
                    for(QueryDocumentSnapshot post: task.getResult()){
                        Map<String, Object> postMap= new HashMap<>(post.getData());
                        images.add(postMap.get("image").toString());
                    }
                    callBackWithParams.onSuccess(images);
                }
                else{
                    callBackWithParams.onFailure(task.getException().getMessage());
                }
            }
        });
    }

    public void getImage(String imageName, imageRecieverCallback imageReciever){
        stRef.child("images").child(imageName).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                imageReciever.onSuccess(bytes);
            }
        });
    }
}
