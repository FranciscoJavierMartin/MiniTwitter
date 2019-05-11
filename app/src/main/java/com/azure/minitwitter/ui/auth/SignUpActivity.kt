package com.azure.minitwitter.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.azure.minitwitter.R
import com.azure.minitwitter.common.Constants
import com.azure.minitwitter.common.SharedPreferencesManager
import com.azure.minitwitter.retrofit.MiniTwitterClient
import com.azure.minitwitter.retrofit.MiniTwitterService
import com.azure.minitwitter.retrofit.request.RequestSignup
import com.azure.minitwitter.retrofit.response.ResponseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    val miniTwitterClient: MiniTwitterClient = MiniTwitterClient.instance
    val miniTwitterService: MiniTwitterService = miniTwitterClient.miniTwitterService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setUpEvents()
    }

    private fun setUpEvents(){
        textViewGoLogin.setOnClickListener { goToLogin() }
        buttonSignUp.setOnClickListener { goToSignUp() }
    }

    private fun goToLogin(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToSignUp(){
        val username: String = editTextUsername.text.toString()
        val email: String = editTextEmail.text.toString()
        val password: String = editTextPassword.text.toString()

        if(username.isEmpty()){
            editTextUsername.error = getString(R.string.username_required)
        } else if(email.isEmpty()){
            editTextEmail.error = getString(R.string.email_required)
        } else if(password.isEmpty()){
            editTextPassword.error = getString(R.string.password_required)
        } else {
            val code = "UDEMYANDROID"
            val requestSignup = RequestSignup(username, email, password, code)
            val call = miniTwitterService.doSignUp(requestSignup)

            call.enqueue(object : Callback<ResponseAuth>{
                override fun onResponse(call: Call<ResponseAuth>, response: Response<ResponseAuth>) {
                    if(response.isSuccessful){

                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_TOKEN, response.body()!!.token)
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_USERNAME, response.body()!!.username)
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_EMAIL, response.body()!!.email)
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_PHOTOURL, response.body()!!.photoUrl)
                        SharedPreferencesManager.setSomeBooleanValue(Constants.PREF_CREATED, response.body()!!.active)

                        val intent = Intent(this, "Something goes wrong", Toast.LENGTH_SHORT)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Something goes wrong", Toast.LENGTH_SHORT)
                    }
                }

                override fun onFailure(call: Call<ResponseAuth>, t: Throwable) {
                    Toast.makeText(this, R.string.main_activity_connection_problem, Toast.LENGTH_SHORT)
                }
            })
        }
    }
}