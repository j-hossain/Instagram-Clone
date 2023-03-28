package com.example.instagramclone;

import java.util.ArrayList;

public interface UserListCallback extends CallBackClass{
    public void setUserList(ArrayList<Model_userInfo> userData);
}
