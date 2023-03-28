package com.example.instagramclone;

import java.util.Map;

public class Model_userInfo {
    private String username;
    private String userID;
    private String useremail;

    public Model_userInfo(String username, String userID, String userEmail) {
        this.username = username;
        this.userID = userID;
        this.useremail = userEmail;
    }
    public Model_userInfo(Map userMap){
        this.username = userMap.get("username").toString();
        this.useremail = userMap.get("email").toString();
        this.userID = userMap.get("id").toString();

    }
    public Model_userInfo(String userID){
        this.userID = userID;
    }
    public String getUsername() {
        return username;
    }

    public String getUseremail() {
        return useremail;
    }

    public String getUserID() {
        return userID;
    }
}
