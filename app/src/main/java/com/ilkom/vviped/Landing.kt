package com.ilkom.vviped

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ilkom.vviped.model.login.Constant
import com.ilkom.vviped.model.login.PreferenceHelper
import com.ilkom.vviped.ui.HomeFragment
import kotlinx.android.synthetic.main.activity_landing.*

class Landing : AppCompatActivity() {
    //    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPref: PreferenceHelper
    private var homeFragment = HomeFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        sharedPref = PreferenceHelper(this)


//        auth = FirebaseAuth.getInstance()

        btn_to_login.setOnClickListener {
            startActivity(Intent(this, UserLogin::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
        btn_to_register.setOnClickListener{
            startActivity(Intent(this, UserRegister::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }

    }

    // if user has logged in
    override fun onStart() {
        super.onStart()
        if(sharedPref.getBoolean(Constant.IS_LOGGED_IN)!!){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
//        if (auth.currentUser != null)
//            startActivity(Intent(this, MainActivity::class.java).also {
//                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            })

    }

}