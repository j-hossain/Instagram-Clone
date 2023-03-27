package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagramclone.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    DatabaseConnector dbConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbConnect = new DatabaseConnector();
//        setContentView(R.layout.activity_register);
        ActivityRegisterBinding registerBinding = DataBindingUtil.setContentView(this,R.layout.activity_register);
        registerBinding.textViewSwitchToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
        registerBinding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = registerBinding.editTextUsername.getText().toString();
                String email = registerBinding.editTextEmail.getText().toString();
                String pass = registerBinding.editTextPassword.getText().toString();
                String confirmPass = registerBinding.editTextConfirmPassword.getText().toString();

                if(username.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Enter all the values", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pass.matches(confirmPass)){
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbConnect.RegisterUser(email, pass, new CallBackClass() {
                    @Override
                    public void onFailure(String msg) {
                        Toast.makeText(RegisterActivity.this, msg   , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(RegisterActivity.this, "Registration Successfull...", Toast.LENGTH_SHORT).show();
                        Toast.makeText(RegisterActivity.this, "Now login with the email", Toast.LENGTH_SHORT).show();
                        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                    }
                });
            }
        });
    }
}