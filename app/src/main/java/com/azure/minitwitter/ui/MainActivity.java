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
import com.azure.minitwitter.retrofit.request.RequestLogin;
import com.azure.minitwitter.retrofit.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLogin;
    TextView tvGoSignUp;
    EditText etEmail, etPassword;
    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        retrofitInit();
        findViews();
        events();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.buttonLogin:
                goToLogin();
                break;
            case R.id.textViewGoSignUp:
                goToSignUp();
                break;
        }
    }

    private void goToSignUp() {
        Intent i = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(i);
    }

    private void goToLogin() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(email.isEmpty()){
            etEmail.setError("Email is required");
        }else if(password.isEmpty()){
            etPassword.setError("Password is required");
        } else {
            RequestLogin requestLogin = new RequestLogin(email, password);

            Call<ResponseAuth> call = miniTwitterService.doLogin(requestLogin);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {

                    if(response.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Login successful",Toast.LENGTH_SHORT );

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

                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(MainActivity.this, "Email or password wrong",Toast.LENGTH_SHORT );
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Connection problem. Please try again",Toast.LENGTH_SHORT );
                }
            });
        }
    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    private void findViews() {
        btnLogin = findViewById(R.id.buttonLogin);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        tvGoSignUp = findViewById(R.id.textViewGoSignUp);
    }

    private void events() {
        btnLogin.setOnClickListener(this);
        tvGoSignUp.setOnClickListener(this);
    }
}
