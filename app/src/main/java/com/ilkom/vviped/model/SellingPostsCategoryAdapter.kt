package com.ilkom.vviped.model

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ilkom.vviped.CategoriesProduct
import com.ilkom.vviped.R
import com.ilkom.vviped.model.login.Constant
import com.ilkom.vviped.model.login.PreferenceHelper
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellingPostsCategoryAdapter(
    private val context: Context,
    private val sellingPosts: MutableList<SellingPostItem>

): RecyclerView.Adapter<SellingPostsCategoryAdapter.ViewHolder>()  {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernamepost = itemView.findViewById<TextView>(R.id.username_post)
        val profpictpost = itemView.findViewById<ImageView>(R.id.user_profpict_post)
        val imagepost = itemView.findViewById<ImageView>(R.id.imagepost_layout)
        val productname = itemView.findViewById<TextView>(R.id.product_name)
        val productcondition = itemView.findViewById<TextView>(R.id.produk_kondisi)
        val campaignname = itemView.findViewById<TextView>(R.id.campaign_title)
        val productprice = itemView.findViewById<TextView>(R.id.harga_produk)
        val productdesc = itemView.findViewById<TextView>(R.id.produk_deskripsi)
        val sellerlocation = itemView.findViewById<TextView>(R.id.lokasi_penjual)
        val buyButton = itemView.findViewById<Button>(R.id.btn_buy)
        val whatsappNumber = itemView.findViewById<TextView>(R.id.whatsapp_number)
        val share_post = itemView.findViewById<ImageButton>(R.id.share_btn)
        val cardViewCategory = itemView.findViewById<CardView>(R.id.cv_productCategory)
        val product_category = itemView.findViewById<TextView>(R.id.item_category)

        val sharedPref = PreferenceHelper(share_post.context)

        fun bindView(sellingPost: SellingPostItem) {
            usernamepost.text = sellingPost.usernamepost
            Picasso.get().load(sellingPost.user_profpict).into(profpictpost)
            Picasso.get().load(sellingPost.image_post).into(imagepost)
            productname.text = sellingPost.product_name
            product_category.text = sellingPost.product_category

            productcondition.text = sellingPost.product_condition
            campaignname.text = sellingPost.campaign_title
            productprice.text = sellingPost.product_price
            productdesc.text = sellingPost.product_description
            sellerlocation.text = sellingPost.seller_location
            whatsappNumber.text = sellingPost.whatsapp

            val prof_pic = sellingPost.user_profpict
            val image_link = sellingPost.image_post
            val image_post =  Picasso.get().load(sellingPost.image_post).into(imagepost).toString()
            val seller_username = usernamepost.text.toString()
            val product_name = productname.text.toString()
            val product_price =  productprice.text.toString()
            val whatsappNumber = whatsappNumber.text.toString()
            val campaign_title = campaignname.text.toString()
            val product_img =  sellingPost.image_post
            val ktg_product_condition = productcondition.text.toString()
            val ktg_product_desc = productdesc.text.toString()
            val ktg_sellerlocation = sellerlocation.text.toString()

            cardViewCategory.setOnClickListener{
                val context = cardViewCategory.context
                val intent = Intent(context, CategoriesProduct::class.java)
                intent.putExtra("kategori", product_category.text.toString() )
                context.startActivity(intent)

                RetrofitInterface().userActivities(
                    sharedPref.getInt(Constant.PREF_ID)!!,
                    sharedPref.getString(Constant.PREF_USERNAME)!!,
                    RequestBody.create(MediaType.parse("multipart/form-data"), "Click category "+product_category.text.toString()),
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
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.sellingposts_category_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: SellingPostsCategoryAdapter.ViewHolder, position: Int) {
        viewHolder.usernamepost.text = sellingPosts[position].usernamepost
        Picasso.get().load(sellingPosts[position].user_profpict).into(viewHolder.profpictpost)
        Picasso.get().load(sellingPosts[position].image_post).into(viewHolder.imagepost)
        viewHolder.productname.text = sellingPosts[position].product_name
        viewHolder.productprice.text = sellingPosts[position].product_price
        viewHolder.productdesc.text = sellingPosts[position].product_description
        viewHolder.sellerlocation.text = sellingPosts[position].seller_location
        viewHolder.whatsappNumber.text = sellingPosts[position].whatsapp
        viewHolder.bindView(sellingPosts[position])

    }

    override fun getItemCount(): Int {
        return sellingPosts.size
    }
}