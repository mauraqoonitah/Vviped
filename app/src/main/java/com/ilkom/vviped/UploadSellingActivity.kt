package com.ilkom.vviped

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ilkom.vviped.model.*
import com.ilkom.vviped.model.SellingPostRepository.create
import com.ilkom.vviped.model.login.Constant
import com.ilkom.vviped.model.login.PreferenceHelper
import kotlinx.android.synthetic.main.activity_upload_selling.*
import kotlinx.android.synthetic.main.activity_upload_selling.backspace
import kotlinx.android.synthetic.main.activity_upload_selling.button_upload
import kotlinx.android.synthetic.main.activity_upload_selling.progress_bar
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class UploadSellingActivity : AppCompatActivity(), UploadRequestBody.UploadCallback {

    private var selectedImageUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_selling)

        val title_campaign = intent.getStringExtra("title_campaign")
        campaign_title.text = title_campaign


        image_view.setOnClickListener {
//            openImageChooser()
            checkPermissionForImage()
        }

        button_upload.setOnClickListener {
            uploadImage()
        }
        backspace.setOnClickListener {
            onBackPressed()
        }

    }



    private fun checkPermissionForImage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                && (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ) {
                val permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionCoarse = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(permission, PERMISSION_CODE_READ)
                requestPermissions(permissionCoarse, PERMISSION_CODE_WRITE)
            } else {
                openImageChooser()
            }
        }
    }
    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, IMAGE_PICK_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            when (requestCode) {
                IMAGE_PICK_CODE -> {
                    selectedImageUri = data?.data
                    image_view.setImageURI(selectedImageUri)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun uploadImage() {
        if (selectedImageUri == null) {
            layout_root.snackbar("Select an Image First")
            return
        }

        val productprice = findViewById<EditText>(R.id.text_priceproduct)
        val productname = findViewById<EditText>(R.id.text_productname)
        val productdesc = findViewById<EditText>(R.id.text_productdesc)
        val sellerlocation = findViewById<EditText>(R.id.text_productloc)
        val radioGroup = findViewById<RadioGroup>(R.id.radiogroup)
        val intSelectButton: Int = radioGroup!!.checkedRadioButtonId
        val radioButton = findViewById<RadioButton>(intSelectButton)
        val whatsapp = findViewById<EditText>(R.id.whatsapp)
        val sharedPref = PreferenceHelper(this)
        val campaign_id = intent.getIntExtra("campaign_id", -1)

        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        progress_bar.progress = 0
        val body = UploadRequestBody(file, "image", this)
        RetrofitInterface().uploadImage(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ),
            RequestBody.create(MediaType.parse("multipart/form-data"), productprice.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"), productname.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"), radioButton.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"), productdesc.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"), sellerlocation.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"), "SALE"),
            RequestBody.create(MediaType.parse("multipart/form-data"), "62"+whatsapp.text.toString()),
            sharedPref.getInt(Constant.PREF_ID)!!,
            campaign_id
        ).enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                layout_root.snackbar(t.message!!)
                progress_bar.progress = 0
            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let { it ->
                    //layout_root.snackbar(it.message)
                    layout_root.snackbar(it.message)
                    progress_bar.progress = 100
                    Log.d("image", it.image)

                    create().detectedObject(it.image).enqueue(object :
                        Callback<List<DetectedItem>> {
                        override fun onResponse(
                            call: Call<List<DetectedItem>>,
                            response: Response<List<DetectedItem>>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("ceeeek", response.body().toString())
//                                response.body()?.let{ it->
//                                    layout_root.snackbar("Objek terdeteksi : "+ it.toString())
//                                }
                                for(c in response.body()!!){
                                    layout_root.snackbar("Berhasil Upload! OBJEK TERDETEKSI: "+ c.object_name.toString() )
                                    RetrofitInterface().uploadDetectionResult(
                                            it.id, c.object_name.toString()
                                    ).enqueue(object : Callback<UploadResponse>{
                                        override fun onResponse(
                                            call: Call<UploadResponse>,
                                            response: Response<UploadResponse>
                                        ) {
//                                            layout_root.snackbar(it.message)
                                        }

                                        override fun onFailure(
                                            call: Call<UploadResponse>,
                                            t: Throwable
                                        ) {
                                            layout_root.snackbar(t.message!!)
                                            progress_bar.progress = 0
                                        }

                                    })
                                }

                            } else {
                                Log.d(
                                    "TAG",
                                    "onResponse: ConfigurationListener::" + call.request().url()
                                )
                            }


                        }

                        override fun onFailure(call: Call<List<DetectedItem>>, t: Throwable) {
                            TODO("Not yet implemented")
                        }


                    })
                }
            }
        })

        RetrofitInterface().userActivities(
            sharedPref.getInt(Constant.PREF_ID)!!,
            sharedPref.getString(Constant.PREF_USERNAME)!!,
            RequestBody.create(MediaType.parse("multipart/form-data"), "Upload new product for campaign "+campaign_title.text.toString()),
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

    override fun onProgressUpdate(percentage: Int) {
        progress_bar.progress = percentage
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
        const val IMAGE_PICK_CODE = 1000;
        const val PERMISSION_CODE_READ = 1001;
        const val PERMISSION_CODE_WRITE = 1002;
    }

}
