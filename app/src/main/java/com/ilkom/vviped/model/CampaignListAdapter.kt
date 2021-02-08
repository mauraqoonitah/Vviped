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
import com.ilkom.vviped.R
import com.ilkom.vviped.UploadSellingActivity
import com.ilkom.vviped.model.login.Constant
import com.ilkom.vviped.model.login.PreferenceHelper
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CampaignListAdapter(
    private var context: Context,
    private var campaignLists: MutableList<CampaignModel>,
    private var isFragment: Boolean = false,
    private var onItemClickCallback: CampaignListAdapter.OnItemClickCallback? =null
) : RecyclerView.Adapter<CampaignListAdapter.ViewHolder>() {

    fun setOnItemClickCallback(onItemClickCallback: CampaignListAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val organizationnamepost = itemView.findViewById<TextView>(R.id.organizationname_post)
        val organizationprofpict_post = itemView.findViewById<ImageView>(R.id.organization_profpict_post)
        val imagecampaign = itemView.findViewById<ImageView>(R.id.imagecampaign_layout)
        val campaignname = itemView.findViewById<TextView>(R.id.campaign_name)
        val campaigndesc = itemView.findViewById<TextView>(R.id.campaign_deskripsi)
        val campaigncategory = itemView.findViewById<TextView>(R.id.campaign_category)
        val campaignreceiver = itemView.findViewById<TextView>(R.id.campaign_penerima)
        val usagedetails = itemView.findViewById<TextView>(R.id.usage_detail)
        val donatebyselling = itemView.findViewById<Button>(R.id.donatewithselling_btn)
        val share_post = itemView.findViewById<ImageButton>(R.id.share_btn)

        val sharedPref = PreferenceHelper(itemView.context)

        fun bindView(campaignItem: CampaignModel) {
            organizationnamepost.text = campaignItem.organization_name
            Picasso.get().load(campaignItem.organization_profpict).into(organizationprofpict_post)
            Picasso.get().load(campaignItem.image_campaign).into(imagecampaign)
            campaignname.text = campaignItem.campaign_title
            campaigndesc.text = campaignItem.campaign_desc
            campaigncategory.text = campaignItem.campaign_category
            campaignreceiver.text = campaignItem.donation_goes
            usagedetails.text = campaignItem.usage_details

            val image_link = campaignItem.image_campaign
            val campaign_id = campaignItem.id
            val campaign_name = campaignname.text.toString()

            donatebyselling.setOnClickListener() {
                val context = donatebyselling.context
                val intent = Intent(context, UploadSellingActivity::class.java)

                intent.putExtra("title_campaign", campaignname.text.toString() )
                intent.putExtra("campaign_id", campaign_id )

                context.startActivity(intent)

                RetrofitInterface().userActivities(
                    sharedPref.getInt(Constant.PREF_ID)!!,
                    sharedPref.getString(Constant.PREF_USERNAME)!!,
                    RequestBody.create(MediaType.parse("multipart/form-data"),
                        "Click Jual Produk Saya for campaign  "+campaignname.text.toString()),
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
                    "Saya mendukung campaign: $campaign_name. " +
                            "Bantu donasi dengan menjual barang kamu atau membeli barang untuk " +
                            "ikut dukung penggalangan dana ini hanya di Vviped!" +
                            " Lihat gambar disini : $image_link" +
                            " -- INSTALL NOW: https://play.google.com/store/apps/details?id=com.ilkom.vviped "
                )
                val sendIntent = Intent.createChooser(shareIntent, null)
                context.startActivity(sendIntent)

            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CampaignListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.campaign_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return campaignLists.size
    }

    override fun onBindViewHolder(viewHolder: CampaignListAdapter.ViewHolder, position: Int) {
        viewHolder.bindView(campaignLists[position])
    }

    interface OnItemClickCallback{
        fun onItemClicked(data:CampaignModel)
    }
}
