package com.ilkom.vviped.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ilkom.vviped.R
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings_contact_us.*

class SettingsContactUs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_contact_us)

        link_to_instagram.setOnClickListener {
            val intent = Intent(this, SettingsContactInstagram::class.java)
            startActivity(intent)

        }

        contact_atikah.setOnClickListener {
            val intent = Intent(this, ContactAtikah::class.java)
            startActivity(intent)
        }
        contact_octa.setOnClickListener {
            val intent = Intent(this, ContactOcta::class.java)
            startActivity(intent)
        }
        contact_maura.setOnClickListener {
            val intent = Intent(this, ContactMaura::class.java)
            startActivity(intent)
        }


    }
}