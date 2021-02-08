package com.ilkom.vviped.model.login

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ilkom.vviped.Landing
import com.ilkom.vviped.MainActivity
import com.ilkom.vviped.R
import com.ilkom.vviped.model.RetrofitInterface
import com.ilkom.vviped.model.UploadResponse
import kotlinx.android.synthetic.main.activity_logout.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Logout : AppCompatActivity() {
    lateinit var sharedPref: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        sharedPref = PreferenceHelper(this)


        btn_no.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        btn_yes.setOnClickListener{
            RetrofitInterface().userActivities(
                sharedPref.getInt(Constant.PREF_ID)!!,
                sharedPref.getString(Constant.PREF_USERNAME)!!,
                RequestBody.create(MediaType.parse("multipart/form-data"), "Logout from apps"),
            ).enqueue(object : Callback<UploadResponse> {
                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<UploadResponse>,
                    response: Response<UploadResponse>
                ) {

                }
            })
            sharedPref.clear()

            startActivity(Intent(this, Landing::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_CLEAR_TOP
            })
            Toast.makeText(this,
                "You've been logged out.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}