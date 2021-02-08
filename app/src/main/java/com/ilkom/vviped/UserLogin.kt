package com.ilkom.vviped

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.ilkom.vviped.model.RetrofitInterface
import com.ilkom.vviped.model.UploadResponse
import com.ilkom.vviped.model.login.Constant
import com.ilkom.vviped.model.login.LoginResponse
import com.ilkom.vviped.model.login.PreferenceHelper
import kotlinx.android.synthetic.main.activity_user_login.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@RequiresApi(Build.VERSION_CODES.KITKAT)
class UserLogin : AppCompatActivity() {
    lateinit var sharedPref: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        sharedPref = PreferenceHelper(this)

        val buttonLogin = findViewById<Button>(R.id.btn_login_account)
        textCreateAccount.setOnClickListener{
            startActivity(Intent(this, UserRegister::class.java))
        }

        buttonLogin.setOnClickListener{
            val login_username = user_name.text.toString().trim()
            val login_password = user_password.text.toString().trim()

            if( login_username.isEmpty()){
                user_name.error = "username cannot be empty"
                user_name.requestFocus()
                return@setOnClickListener
            }
            if( login_username.length < 6){
                user_name.error = "Username too short"
                user_name.requestFocus()
                return@setOnClickListener
            }
            if(login_password.isEmpty()){
                user_password.error = "field cannot be empty"
                user_password.requestFocus()
                return@setOnClickListener
            }
            if(login_password.length < 6){
                user_password.error = "Password too short"
                user_password.requestFocus()
                return@setOnClickListener
            }
            userLogin()
        }



    }

    private fun userLogin() {
        val username = findViewById<EditText>(R.id.user_name)
        val password = findViewById<EditText>(R.id.user_password)

        val login_username = user_name.text.toString().trim()
        val login_password = user_password.text.toString().trim()

        RetrofitInterface().loginUser(
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                username.text.toString()
            ),
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                password.text.toString()
            ),
        ).enqueue(object : Callback<LoginResponse> {

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                val snackbar = t.message?.let {
                    Snackbar
//                            .make(layout_userLogin, t.message!!, Snackbar.LENGTH_LONG)
                        .make(layout_userLogin, "Username/password doesn't exist!", Snackbar.LENGTH_LONG)
                }
                snackbar?.show()
            }

            override fun onResponse(
                call: Call<LoginResponse>, response: Response<LoginResponse>
            ) {
                response.body()?.let {
                    if(response.body() != null && response.body()!!.isStatus()!!){
                        val login_id = response.body()!!.data!!.id
                        val login_email = response.body()!!.data!!.email
                        val login_fullname = response.body()!!.data!!.fullname
                        val login_profpic = response.body()!!.data!!.user_profpic
                        // jika password benar
                        Toast.makeText(
                            this@UserLogin,
                            "Welcome, " + response.body()!!.data!!.fullname + "!",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        if (login_id != null) {
                            if (login_email != null) {
                                if (login_fullname != null) {
                                    if (login_profpic != null) {
                                        saveSession(login_id, login_username, login_password, login_email, login_fullname, login_profpic)
                                    }
                                }
                            }
                        }
                        nextToMainActivity()

                        RetrofitInterface().userActivities(
                            sharedPref.getInt(Constant.PREF_ID)!!,
                            sharedPref.getString(Constant.PREF_USERNAME)!!,
                            RequestBody.create(MediaType.parse("multipart/form-data"), "Login apps"),
                        ).enqueue(object : Callback<UploadResponse> {
                            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                            }

                            override fun onResponse(
                                call: Call<UploadResponse>,
                                response: Response<UploadResponse>
                            ) {

                            }
                        })
                    } else {
                        // jika salah password
                        Toast.makeText(this@UserLogin, response.body()!!.message, Toast.LENGTH_LONG)
                            .show()

                    }
                }
            }
        })
    }

    private fun nextToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })

    }

    override fun onStart() {
        super.onStart()
        if(sharedPref.getBoolean(Constant.IS_LOGGED_IN)!!){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }


    private fun saveSession(
        id: Int,
        username: String,
        password: String,
        email: String,
        fullname: String,
        profpic: String
    ) {
        sharedPref.put(Constant.PREF_ID, id)
        sharedPref.put(Constant.PREF_USERNAME, username)
        sharedPref.put(Constant.PREF_PASSWORD, password)
        sharedPref.put(Constant.PREF_EMAIL, email)
        sharedPref.put(Constant.PREF_FULLNAME, fullname)
        sharedPref.put(Constant.PREF_PROFPIC, profpic)
        sharedPref.put(Constant.IS_LOGGED_IN, true)

    }


}