package com.ilkom.vviped.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.ilkom.vviped.R
import kotlinx.android.synthetic.main.activity_contact_maura.*
import kotlinx.android.synthetic.main.activity_settings_contact_instagram.*
import kotlinx.android.synthetic.main.activity_settings_contact_instagram.webView_instagram

class ContactMaura : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_maura)

        webView_contact_maura.settings.javaScriptEnabled = true
        webView_contact_maura.webViewClient = WebViewClient()
        webView_contact_maura.loadUrl("https://www.instagram.com/mauraputri/")

    }
}