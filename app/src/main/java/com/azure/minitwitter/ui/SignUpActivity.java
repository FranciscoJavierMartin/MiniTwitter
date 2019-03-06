package com.azure.minitwitter.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.azure.minitwitter.R;
import com.azure.minitwitter.common.Constants;
import com.azure.minitwitter.common.SharedPreferencesManager;
import com.azure.minitwitter.retrofit.MiniTwitterClient;
import com.azure.minitwitter.retrofit.MiniTwitterService;
import com.azure.minitwitter.retrofit.request.RequestSignup;
import com.azure.minitwitter.retrofit.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnSignUp;
    TextView tvGoLogin;
    EditText etUsername, etEmail, etPassword;
    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        retrofitInit();
        findViews();
        events();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.buttonSignUp:
                goToSignUp();
                break;
            case R.id.textViewGoLogin:
                goToLogin();
                break;
        }
    }

    private void goToSignUp() {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(username.isEmpty()){
            etUsername.setError("Username is required");
        }else if(email.isEmpty()){
            etEmail.setError("Email is required");
        }else if(password.isEmpty()){
            etPassword.setError("Password is required");
        } else {
            String code = "UDEMYANDROID";
            RequestSignup requestSignup = new RequestSignup(username,email,password,code);
            Call<ResponseAuth> call = miniTwitterService.doSignUp(requestSignup);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()) {
                        SharedPreferencesManager
                                .setSomeStringValue(Constants.PREF_TOKEN,response.body().getToken());
                        SharedPreferencesManager
                                .setSomeStringValue(Constants.PREF_USERNAME,response.body().getUsername());
                        SharedPreferencesManager
                                .setSomeStringValue(Constants.PREF_EMAIL,response.body().getEmail());
                        SharedPreferencesManager
                                .setSomeStringValue(Constants.PREF_PHOTOURL,response.body().getPhotoUrl());
                        SharedPreferencesManager
                                .setSomeBooleanValue(Constants.PREF_CREATED,response.body().getActive());
                        Intent intent = new Intent(SignUpActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(SignUpActivity.this, "Something goes wrong", Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Error on connection. Please try again", Toast.LENGTH_SHORT);
                }
            });
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

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    private void findViews(){
        btnSignUp = findViewById(R.id.buttonSignUp);
        tvGoLogin = findViewById(R.id.textViewGoLogin);
        etUsername = findViewById(R.id.editTextUsername);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
    }

    private void events(){
        tvGoLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }
}
