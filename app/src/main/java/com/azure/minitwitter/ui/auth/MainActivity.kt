package com.azure.minitwitter.ui.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.azure.minitwitter.R
import com.azure.minitwitter.common.Constants
import com.azure.minitwitter.common.SharedPreferencesManager
import com.azure.minitwitter.retrofit.MiniTwitterClient
import com.azure.minitwitter.retrofit.MiniTwitterService
import com.azure.minitwitter.retrofit.request.RequestLogin
import com.azure.minitwitter.retrofit.response.ResponseAuth
import com.azure.minitwitter.ui.DashboardActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val miniTwitterClient: MiniTwitterClient = MiniTwitterClient.instance
    private val miniTwitterService: MiniTwitterService = miniTwitterClient.miniTwitterService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setUpEvents()
    }

    private fun setUpEvents(){
        buttonLogin.setOnClickListener { goToLogin() }
        textViewGoSignUp.setOnClickListener { goToSignUp() }
    }

    private fun goToSignUp(){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun goToLogin(){
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if(email.isEmpty()){
            editTextEmail.error = getString(R.string.email_required)
        } else if(password.isEmpty()){
            editTextPassword.error = getString(R.string.password_required)
        } else {
            val requestLogin = RequestLogin(email, password)
            val call = miniTwitterService.doLogin(requestLogin)
            call.enqueue(object : Callback<ResponseAuth>{
                override fun onResponse(call: Call<ResponseAuth>, response: Response<ResponseAuth>) {
                    if(response.isSuccessful){
                        Toast.makeText(this, R.string.main_activity_login_successful, Toast.LENGTH_SHORT)

                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_TOKEN, response.body()!!.token)
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_USERNAME, response.body()!!.username)
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_EMAIL, response.body()!!.email)
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_PHOTOURL, response.body()!!.photoUrl)
                        SharedPreferencesManager.setSomeBooleanValue(Constants.PREF_CREATED, response.body()!!.active)

                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, R.string.main_activity_email_password_wrong, Toast.LENGTH_SHORT)
                    }
                }

                override fun onFailure(call: Call<ResponseAuth>, t: Throwable) {
                    Toast.makeText(this, R.string.main_activity_connection_problem, Toast.LENGTH_SHORT)
                }
            })
        }
    }
}