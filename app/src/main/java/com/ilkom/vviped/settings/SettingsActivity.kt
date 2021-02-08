package com.ilkom.vviped.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ilkom.vviped.EditProfileUser
import com.ilkom.vviped.R
import com.ilkom.vviped.model.RetrofitInterface
import com.ilkom.vviped.model.UploadResponse
import com.ilkom.vviped.model.login.Constant
import com.ilkom.vviped.model.login.Logout
import com.ilkom.vviped.model.login.PreferenceHelper
import kotlinx.android.synthetic.main.activity_settings.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPref = PreferenceHelper(this)

        btn_settings_about.setOnClickListener{
            val intent = Intent(this, SettingsAbout::class.java)
            startActivity(intent)
        }

        btn_settings_contact.setOnClickListener{
            val intent = Intent(this, SettingsContactUs::class.java)
            startActivity(intent)
        }

        btn_settings_logout.setOnClickListener{
            val intent = Intent(this, Logout::class.java)
            startActivity(intent)

            RetrofitInterface().userActivities(
                sharedPref.getInt(Constant.PREF_ID)!!,
                sharedPref.getString(Constant.PREF_USERNAME)!!,
                RequestBody.create(MediaType.parse("multipart/form-data"), "Click Logout button"),
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
        btn_settings_edit_profile.setOnClickListener {
            val intent = Intent(this, EditProfileUser::class.java)
            startActivity(intent)

            RetrofitInterface().userActivities(
                sharedPref.getInt(Constant.PREF_ID)!!,
                sharedPref.getString(Constant.PREF_USERNAME)!!,
                RequestBody.create(MediaType.parse("multipart/form-data"), "Click Ubah Profil button"),
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


    }
}