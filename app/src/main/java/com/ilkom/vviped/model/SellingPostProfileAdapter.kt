package com.ilkom.vviped.model

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.ilkom.vviped.EditProduct
import com.ilkom.vviped.R
import com.ilkom.vviped.model.login.Constant
import com.ilkom.vviped.model.login.Constant.Companion.PREF_ID
import com.ilkom.vviped.model.login.PreferenceHelper
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class SellingPostProfileAdapter(
    private val context: Context,
    private val sellingPosts: MutableList<SellingPostItem>


) : RecyclerView.Adapter<SellingPostProfileAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val usernamepost = itemView.findViewById<TextView>(R.id.usernamepost)
        val profpictpost = itemView.findViewById<ImageView>(R.id.userprofpict_post)
        val imagepost = itemView.findViewById<ImageView>(R.id.imagepostlayout)
        val productname = itemView.findViewById<TextView>(R.id.productname)
        val productcondition = itemView.findViewById<TextView>(R.id.produkkondisi)
        val campaignname = itemView.findViewById<TextView>(R.id.campaigntitle)
        val productprice = itemView.findViewById<TextView>(R.id.hargaproduk)
        val productdesc = itemView.findViewById<TextView>(R.id.produkdeskripsi)
        val sellerlocation = itemView.findViewById<TextView>(R.id.lokasipenjual)
        val whatsappNumber = itemView.findViewById<TextView>(R.id.whatsapp_number)
        val buttonContextMenu = itemView.findViewById<ImageButton>(R.id.btn_context_menu)


        fun bindView(sellingPost: SellingPostItem) {
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

            val sharedPref = PreferenceHelper(buttonContextMenu.context)

            val product_id = sellingPost.id
            val productId = product_id.toString()

            val product_name = productname.text.toString()
            val product_price =  productprice.text.toString()
            val campaign_title = campaignname.text.toString()
            val product_desc = productdesc.text.toString()
            val product_loc = sellerlocation.text.toString()
            val no_whatsapp = whatsappNumber.text.toString()


            //menu  edit dan hapus product post
            buttonContextMenu.setOnClickListener {

                val context = buttonContextMenu.context

                val pop= PopupMenu(context, it)
                pop.inflate(R.menu.context_menu_post)
                pop.setOnMenuItemClickListener { item ->
                    when(item.itemId){
                        R.id.menu_edit_post->{
                            val context = buttonContextMenu.context
                            val intent = Intent(context, EditProduct::class.java)
                            intent.putExtra("nama_campaign", campaign_title )
                            intent.putExtra("nama_produk", product_name )
                            intent.putExtra("harga_produk", product_price )
                            intent.putExtra("deskripsi_produk", product_desc )
                            intent.putExtra("lokasi_produk", product_loc )
                            intent.putExtra("whatsapp_penjual", no_whatsapp )
                            intent.putExtra("id_product", sellingPost.id.toString() )
                            context.startActivity(intent)

                            RetrofitInterface().userActivities(
                                sharedPref.getInt(Constant.PREF_ID)!!,
                                sharedPref.getString(Constant.PREF_USERNAME)!!,
                                RequestBody.create(MediaType.parse("multipart/form-data"), "Click edit product "+productname.text.toString()),
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
                        R.id.menu_delete_post->{

                            AlertDialog.Builder(context)

                                .setMessage("Hapus barang ini?")
                                .setPositiveButton("Ya",  { dialogInterface, i ->

                                    RetrofitInterface().deleteProductProfile(
                                        RequestBody.create(
                                            MediaType.parse("multipart/form-data"),
                                            productId
                                        ),
                                    ).enqueue(object : Callback<MutableList<SellingPostItem>> {

                                        override fun onFailure(call: Call<MutableList<SellingPostItem>>, t: Throwable) {
                                            Toast.makeText(context, "Gagal dihapus.", Toast.LENGTH_SHORT).show()
                                        }
                                        override fun onResponse(
                                            call: Call<MutableList<SellingPostItem>>,
                                            response: Response<MutableList<SellingPostItem>>
                                        ) {
                                            Toast.makeText(context, "Berhasil dihapus! Swipe down untuk refresh halaman", Toast.LENGTH_LONG).show()
                                        }
                                    })
                                    RetrofitInterface().userActivities(
                                        sharedPref.getInt(Constant.PREF_ID)!!,
                                        sharedPref.getString(Constant.PREF_USERNAME)!!,
                                        RequestBody.create(MediaType.parse("multipart/form-data"), "Delete product "+productname.text.toString()),
                                    ).enqueue(object : Callback<UploadResponse> {
                                        override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                                        }

                                        override fun onResponse(
                                            call: Call<UploadResponse>,
                                            response: Response<UploadResponse>
                                        ) {

                                        }
                                    })
                                })
                                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                                })
                                .show()

                        }
                        R.id.menu_share_post->{
                            val context = buttonContextMenu.context
                            val shareIntent = Intent()
                            shareIntent.action = Intent.ACTION_SEND
                            shareIntent.type="text/plain"
                            shareIntent.putExtra(
                                Intent.EXTRA_TEXT,
                                "Bantu berdonasi dengan membeli barang: $product_name " +
                                        "seharga Rp$product_price " +
                                        "guna mendukung galang dana untuk campaign: $campaign_title. " +
                                        "Beli barangnya sekarang hanya di Vviped! http://bit.ly/Vviped_App "
                            )
                            val sendIntent = Intent.createChooser(shareIntent, "Bagikan product ini ")
                            context.startActivity(sendIntent)
                        }
                    }
                    true
                }
                // munculin icon
                try {
                    val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                    fieldMPopup.isAccessible = true
                    val mPopup = fieldMPopup.get(pop)
                    mPopup.javaClass
                        .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                        .invoke(mPopup, true)
                } catch (e: Exception){
                    Log.e("Main", "Error showing menu icons", e )

                } finally {
                    pop.show()
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.sellingpostprofile_layout, parent, false)
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
        viewHolder.bindView(sellingPosts[position])




    }

    override fun getItemCount(): Int {
        return sellingPosts.size
    }
}


