package com.ilkom.vviped.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.ilkom.vviped.R
import kotlinx.android.synthetic.main.activity_contact_atikah.*
import kotlinx.android.synthetic.main.activity_settings_contact_instagram.*
import kotlinx.android.synthetic.main.activity_settings_contact_instagram.webView_instagram

class ContactAtikah : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_atikah)

        webView_contact_atikah.settings.javaScriptEnabled = true
        webView_contact_atikah.webViewClient = WebViewClient()
        webView_contact_atikah.loadUrl("https://www.instagram.com/atikahbzqaulia/")

    }
}