package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail,editTextPassword;
    private Button buttonLogin;
    private TextView textViewSwitchToRegister;

    DatabaseConnector dbConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSwitchToRegister = findViewById(R.id.textViewSwitchToRegister);
        dbConnect = new DatabaseConnector();

        textViewSwitchToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regiterIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regiterIntent);
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                dbConnect.LoginUser(email, password, new CallBackClass() {
                    @Override
                    public void onFailure(String msg) {
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {
                        Intent homeIntent = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(homeIntent);
                    }
                });
            }
        });
    }
}