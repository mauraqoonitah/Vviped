package com.ilkom.vviped

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.ilkom.vviped.model.login.PreferenceHelper
import com.ilkom.vviped.model.OnBoardingData

class IntroSlider : AppCompatActivity() {

    var onBoardingViewPagerAdapter: OnBoardingViewPagerAdapter? = null
    var tabLayout: TabLayout? = null
    var onBoardingViewPager: ViewPager? = null
    var nextSlide: TextView? = null
    var position = 0
    var sharedPreferences: SharedPreferences? =  null
    lateinit var sharedPref: PreferenceHelper



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = PreferenceHelper(this)


        if(restorePrefData()) {
            val i = Intent(applicationContext, Landing::class.java)
            startActivity(i)
        }

        setContentView(R.layout.activity_intro_slider)

        tabLayout = findViewById(R.id.tab_indicator)
        nextSlide = findViewById(R.id.next_slide)
        val onBoardingData: MutableList<OnBoardingData> = ArrayList()

        onBoardingData.add(OnBoardingData(
            "WELCOME TO VVIPED",
            "Aplikasi media sosial untuk penggalangan dana secara online " +
                    "dengan cara jual-beli barang sekaligus berdonasi.",
            R.drawable.slider_welcome))

        onBoardingData.add(OnBoardingData(
            "JUAL UNTUK DONASI",
            "Jual produk kamu dan hasil penjualan disumbangkan " +
                    "ke penggalangan dana milik kamu ataupun orang lain." ,
            R.drawable.slider_jualuntukdonasi))

        onBoardingData.add(OnBoardingData(
            "CARA BUAT CAMPAIGN",
            "Tekan tombol tanda tambah pada halaman Daftar Campaign untuk buat penggalangan dana",
            R.drawable.slider_buat_campaign))

        onBoardingData.add(OnBoardingData(
            "CARA JUAL PRODUK",
            "Tekan tombol 'Jual Produk Saya' di penggalangan dana yang ingin kamu donasikan " +
                    "yang ada pada halaman Daftar Campaign untuk menjual produk kamu",
            R.drawable.slider_jualproduksaya))

        onBoardingData.add(OnBoardingData(
            "",
            "",
            R.drawable.slider_getstarted))


        setOnBoardingViewPagerAdapter(onBoardingData)

        position = onBoardingViewPager!!.currentItem

        nextSlide?.setOnClickListener {

            if (position < onBoardingData.size) {
                position++
                onBoardingViewPager!!.currentItem = position
            }
            if(position == onBoardingData.size){
                savePrefData()
                val i = Intent(applicationContext, Landing::class.java)
                startActivity(i)
            }
            tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    position = tab!!.position
                    if (tab.position == onBoardingData.size - 1) {
                        nextSlide!!.text = "MULAI"
                    } else {
                        nextSlide!!.text = "Next"

                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })


        }

    }

    private fun setOnBoardingViewPagerAdapter(onBoardingData: MutableList<OnBoardingData>) {
        onBoardingViewPager = findViewById(R.id.screenPager)
        onBoardingViewPagerAdapter = OnBoardingViewPagerAdapter(this, onBoardingData)
        onBoardingViewPager!!.adapter = onBoardingViewPagerAdapter
        tabLayout?.setupWithViewPager(onBoardingViewPager)
    }

    private fun savePrefData(){
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences!!.edit()
        editor.putBoolean("isFirstTimeRun", true)
        editor.apply()
    }

    private fun restorePrefData(): Boolean{
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("isFirstTimeRun", false)

    }

}