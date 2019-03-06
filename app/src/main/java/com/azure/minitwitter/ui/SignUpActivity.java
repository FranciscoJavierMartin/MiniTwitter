package com.azure.minitwitter.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.azure.minitwitter.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnSignUp;
    TextView tvGoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();


        btnSignUp = findViewById(R.id.buttonSignUp);
        btnSignUp.setOnClickListener(this);

        tvGoLogin = findViewById(R.id.textViewGoLogin);
        tvGoLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.buttonSignUp:
                break;
            case R.id.textViewGoLogin:
                goToLogin();
                break;
        }
    }

    private void goToLogin(){
        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}