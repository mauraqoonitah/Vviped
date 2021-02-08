package com.ilkom.vviped

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ilkom.vviped.model.RetrofitInterface
import com.ilkom.vviped.model.UploadResponse
import com.ilkom.vviped.model.login.Constant
import com.ilkom.vviped.model.login.PreferenceHelper
import com.ilkom.vviped.model.snackbar
import kotlinx.android.synthetic.main.activity_edit_product.*
import kotlinx.android.synthetic.main.activity_edit_product.button_save
import kotlinx.android.synthetic.main.activity_edit_product.progress_bar
import kotlinx.android.synthetic.main.activity_edit_profile_user.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProduct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        val sharedPref = PreferenceHelper(this)

        campaign_title.text = intent.getStringExtra("nama_campaign")
        namaproduk_edit.setText(intent.getStringExtra("nama_produk"))
        edit_priceproduct.setText(intent.getStringExtra("harga_produk"))
        edit_productdesc.setText(intent.getStringExtra("deskripsi_produk"))
        edit_productloc.setText(intent.getStringExtra("lokasi_produk"))
        edit_whatsapp.setText(intent.getStringExtra("whatsapp_penjual"))
        //edit_whatsapp.setText(intent.getStringExtra("id_product"))

        backspace.setOnClickListener {
            onBackPressed()
        }

        button_save.setOnClickListener {
            saveChanges()
            RetrofitInterface().userActivities(
                sharedPref.getInt(Constant.PREF_ID)!!,
                sharedPref.getString(Constant.PREF_USERNAME)!!,
                RequestBody.create(MediaType.parse("multipart/form-data"), "Save changes for product "+namaproduk_edit.text.toString()),
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

    private fun saveChanges() {

        val id_sellingpost = intent.getStringExtra("id_product")!!.toInt()

        RetrofitInterface().editProduct(
            id_sellingpost,
            RequestBody.create(MediaType.parse("multipart/form-data"), edit_priceproduct.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"), namaproduk_edit.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"), edit_productdesc.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"), edit_productloc.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"), edit_whatsapp.text.toString())
        ).enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                layout_editproduct.snackbar(t.message!!)
                progress_bar.progress = 0
            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    layout_editproduct.snackbar(it.message)
                    progress_bar.progress = 100
                }
            }
        })
    }
}
