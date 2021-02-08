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
import androidx.recyclerview.widget.RecyclerView
import com.ilkom.vviped.ChatForBuying
import com.ilkom.vviped.R
import com.ilkom.vviped.model.login.Constant
import com.ilkom.vviped.model.login.PreferenceHelper
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchAdapter(
    private val context: Context,
    private val sellingPosts: MutableList<SellingPostItem>
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>()
{
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val usernamepost = itemView.findViewById<TextView>(R.id.username_post3)
        val profpictpost = itemView.findViewById<ImageView>(R.id.user_profpict_post3)
        val imagepost = itemView.findViewById<ImageView>(R.id.imagepost_layout3)
        val productname = itemView.findViewById<TextView>(R.id.product_name3)
        val productcondition = itemView.findViewById<TextView>(R.id.produk_kondisi3)
        val campaignname = itemView.findViewById<TextView>(R.id.campaign_title3)
        val productprice = itemView.findViewById<TextView>(R.id.harga_produk3)
        val productdesc = itemView.findViewById<TextView>(R.id.produk_deskripsi3)
        val sellerlocation = itemView.findViewById<TextView>(R.id.lokasi_penjual3)
        val buyButton = itemView.findViewById<Button>(R.id.btn_buy3)
        val whatsappNumber = itemView.findViewById<TextView>(R.id.whatsapp_number3)
        val share_post = itemView.findViewById<ImageButton>(R.id.share_btn3)

        val sharedPref = PreferenceHelper(share_post.context)


        fun bindView (sellingPost: SellingPostItem){
            usernamepost.text = sellingPost.usernamepost
            Picasso.get().load(sellingPost.user_profpict).into(profpictpost)
            Picasso.get().load(sellingPost.image_post).into(imagepost)
            productname.text = sellingPost.product_name
            productcondition.text = sellingPost.product_condition
            campaignname.text = sellingPost.campaign_title
            productprice.text = sellingPost.product_price
            productdesc.text = sellingPost.product_description
            sellerlocation.text = sellingPost.seller_location
            whatsappNumber.text = sellingPost.whatsapp

            val image_link = sellingPost.image_post
            val seller_username = usernamepost.text.toString()
            val product_name = productname.text.toString()
            val product_price =  productprice.text.toString()
            val whatsappNumber = whatsappNumber.text.toString()
            val campaign_title = campaignname.text.toString()
            val product_img =  sellingPost.image_post

            buyButton.setOnClickListener {
                val context = buyButton.context
                val intent = Intent(context, ChatForBuying::class.java)
                intent.putExtra("seller_username", seller_username )
                intent.putExtra("product_name", product_name )
                intent.putExtra("product_price", product_price )
                intent.putExtra("campaign_title", sellingPost.campaign_title.toString() )
                intent.putExtra("whatsapp", whatsappNumber )
                context.startActivity(intent)
                RetrofitInterface().userActivities(
                    sharedPref.getInt(Constant.PREF_ID)!!,
                    sharedPref.getString(Constant.PREF_USERNAME)!!,
                    RequestBody.create(MediaType.parse("multipart/form-data"), "Click Beli for product "+productname.text.toString()),
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
            share_post.setOnClickListener{
                val context = share_post.context
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type="text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "Yuk, bantu berdonasi dengan membeli barang: $product_name " +
                            "seharga Rp$product_price " +
                            "guna mendukung galang dana untuk campaign: $campaign_title. " +
                            "Beli barangnya sekarang juga hanya di Vviped! Lihat gambar disini: $image_link ." +
                            " -- INSTALL NOW: https://play.google.com/store/apps/details?id=com.ilkom.vviped "
                )
                val sendIntent = Intent.createChooser(shareIntent, null)
                context.startActivity(sendIntent)
            }

    }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.sellingpostcategory_layout, parent, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
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

