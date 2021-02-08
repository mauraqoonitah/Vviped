package com.ilkom.vviped

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.ilkom.vviped.model.RetrofitInterface
import com.ilkom.vviped.model.UploadResponse
import com.ilkom.vviped.model.login.PreferenceHelper
import kotlinx.android.synthetic.main.activity_user_register.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRegister : AppCompatActivity() {
    lateinit var sharedPref: PreferenceHelper

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)

        sharedPref = PreferenceHelper(this)


        btn_register_account.setOnClickListener {

            val email = user_email.text.toString().trim()
            val fullname = user_fullname.text.toString().trim()
            val username = user_name.text.toString().trim()
            val password = user_password.text.toString().trim()

            if(email.isEmpty() ){
                user_email.error = "field cannot be empty"
                user_email.requestFocus()
                return@setOnClickListener
            }
            if(fullname.isEmpty()){
                user_fullname.error = "Name field cannot be empty"
                user_fullname.requestFocus()
                return@setOnClickListener
            }
            if(username.isEmpty()){
                user_name.error = "username field cannot be empty"
                user_name.requestFocus()
                return@setOnClickListener
            }
            if(username.length < 6){
                user_name.error = "Username too short"
                user_name.requestFocus()
                return@setOnClickListener
            }
            if(password.isEmpty()){
                user_password.error = "field cannot be empty"
                user_password.requestFocus()
                return@setOnClickListener
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                user_email.error = "Email not valid"
                user_email.requestFocus()
                return@setOnClickListener
            }

            if(password.length < 6){
                user_password.error = "Password too short"
                user_password.requestFocus()
                return@setOnClickListener
            }
            register()

        }

        imageBackspace.setOnClickListener{
            startActivity(Intent(this, Landing::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }

        textLoginHere.setOnClickListener{
            sharedPref.clear()
            startActivity(Intent(this, UserLogin::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun register() {
        val email = findViewById<EditText>(R.id.user_email)
        val fullname = findViewById<EditText>(R.id.user_fullname)
        val username = findViewById<EditText>(R.id.user_name)
        val password = findViewById<EditText>(R.id.user_password)

        RetrofitInterface().registerUser(
            RequestBody.create(MediaType.parse("multipart/form-data"), email.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"), fullname.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"), username.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"), password.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"), "profpic/profpic_default.png")

        ).enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
//                    layout_userRegister.snackbar(t.message!!)
                val snackbar = t.message?.let {
                    Snackbar.make(layout_userRegister, "Email/username already exist!", Snackbar.LENGTH_LONG)
                }
                snackbar?.show()
            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    RetrofitInterface().userActivities(
                        it.id,
                        username.text.toString(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), "Register account"),
                    ).enqueue(object : Callback<UploadResponse> {
                        override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                        }

                        override fun onResponse(
                            call: Call<UploadResponse>,
                            response: Response<UploadResponse>
                        ) {

                        }
                    })
                }
                nextActivity()
            }
        })
    }

    private fun nextActivity() {
        startActivity(Intent(this, UserLogin::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        Toast.makeText(
            this,
            "Success! Please Login.",
            Toast.LENGTH_LONG
        ).show()
    }
}

