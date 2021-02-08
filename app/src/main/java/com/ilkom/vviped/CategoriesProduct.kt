package com.ilkom.vviped

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilkom.vviped.model.RetrofitInterface
import com.ilkom.vviped.model.SearchAdapter
import com.ilkom.vviped.model.SellingPostItem
import com.ilkom.vviped.model.SellingPostsAdapter
import kotlinx.android.synthetic.main.activity_categories_product.*
import kotlinx.android.synthetic.main.activity_categories_product.backspace_btn
import kotlinx.android.synthetic.main.activity_search.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesProduct : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var searchAdapter: SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_product)

        backspace_btn.setOnClickListener {
            onBackPressed()
        }
        val kategori = intent.getStringExtra("kategori")
        title_category_appbar.text = kategori

        getDataCategory()

    }

    private fun getDataCategory(){

        val kategori = intent.getStringExtra("kategori")!!.toString()

        recycleView_categories.setHasFixedSize(true)
        recycleView_categories.layoutManager = LinearLayoutManager(this)

        RetrofitInterface().getFeedCategory(
            kategori
        ).enqueue(object : Callback<MutableList<SellingPostItem>> {
            override fun onResponse(
                call: Call<MutableList<SellingPostItem>>,
                response: Response<MutableList<SellingPostItem>>
            ) {
                searchAdapter = getApplicationContext().let { SearchAdapter(it, response.body() as MutableList<SellingPostItem>) }
                recycleView_categories.adapter = searchAdapter
                searchAdapter?.notifyDataSetChanged()

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