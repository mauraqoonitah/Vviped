package com.ilkom.vviped.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.ilkom.vviped.R
import kotlinx.android.synthetic.main.activity_settings_contact_instagram.*

class SettingsContactInstagram : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_contact_instagram)

        webView_instagram.settings.javaScriptEnabled = true
        webView_instagram.webViewClient = WebViewClient()
        webView_instagram.loadUrl("https://instagram.com/vviped")
    }
}