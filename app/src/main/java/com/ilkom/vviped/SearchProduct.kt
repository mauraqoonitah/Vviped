package com.ilkom.vviped

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilkom.vviped.model.*
import com.ilkom.vviped.model.login.Constant
import com.ilkom.vviped.settings.SettingsActivity
import com.ilkom.vviped.ui.HomeFragment
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchProduct : AppCompatActivity() {
    private val sellingPosts = ArrayList<SellingPostItem>()
    private var searchAdapter: SearchAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val EditTextSearch = findViewById<EditText>(R.id.et_search_product)
        EditTextSearch.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                getDataSearch()
                return@OnKeyListener true
            }
            false
        })

        backspace_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getDataSearch(){

        val EditTextSearch = findViewById<EditText>(R.id.et_search_product)
        val searchEditTextValue = EditTextSearch.text.toString()

        recycleView_search.setHasFixedSize(true)
        recycleView_search.layoutManager = LinearLayoutManager(this)

       RetrofitInterface().getFeedSearch(
           RequestBody.create(
               MediaType.parse("multipart/form-data"),
               searchEditTextValue
           )
       ).enqueue(object : Callback<MutableList<SellingPostItem>> {
            override fun onResponse(
                call: Call<MutableList<SellingPostItem>>,
                response: Response<MutableList<SellingPostItem>>
            ) {
                searchAdapter = getApplicationContext().let { SearchAdapter(it, response.body() as MutableList<SellingPostItem>) }
                recycleView_search.adapter = searchAdapter
                searchAdapter?.notifyDataSetChanged()

                EditTextSearch.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                        getDataSearch()
                        return@OnKeyListener true
                    }
                    false
                })
            }

            override fun onFailure(call: Call<MutableList<SellingPostItem>>, t: Throwable) {
                Log.e("tag", t.toString())
//                Toast.makeText(
//                    this@SearchProduct,
//                    "tidak ditemukan",
//                    Toast.LENGTH_SHORT)
//                    .show()
            }

        })




    }

}
