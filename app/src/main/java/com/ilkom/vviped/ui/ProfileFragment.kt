package com.ilkom.vviped.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilkom.vviped.*
import com.ilkom.vviped.model.*
import com.ilkom.vviped.model.login.Constant
import com.ilkom.vviped.model.login.PreferenceHelper
import com.ilkom.vviped.settings.SettingsActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_login.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var sellingPostProfileAdapter: SellingPostProfileAdapter? = null
    private var campaignListProfileAdapter: CampaignListProfileAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        getSellingPostData()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = PreferenceHelper(requireActivity())

        view.text_username.text = sharedPref.getString(Constant.PREF_USERNAME)
        view.text_fullname.text = sharedPref.getString(Constant.PREF_FULLNAME)
        view.user_email.text = sharedPref.getString(Constant.PREF_EMAIL)
        Picasso.get().load(sharedPref.getString(Constant.PREF_PROFPIC)).into(profpic)


        btn_settings.setOnClickListener {
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
        }

        product_btn.setOnClickListener {
            getSellingPostData()
        }

        campaign_btn.setOnClickListener {
            getCampaignListData()
        }

        recyclerView = view.findViewById(R.id.recycleView_profile)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

    }

    private fun refreshCampaign() {
        swipeToRefreshProfileLayout.setOnRefreshListener {
            getCampaignListData()
            swipeToRefreshProfileLayout.isRefreshing = false
        }
    }
    private fun refreshProduct() {
        swipeToRefreshProfileLayout.setOnRefreshListener {
            getSellingPostData()
            swipeToRefreshProfileLayout.isRefreshing = false
        }
    }


    fun getSellingPostData(){
        val sharedPref = PreferenceHelper(requireActivity())

        RetrofitInterface().sellingPostProfile(
            sharedPref.getInt(Constant.PREF_ID)!!
        ).enqueue(object : Callback<MutableList<SellingPostItem>> {

            override fun onResponse(
                call: Call<MutableList<SellingPostItem>>,
                response: Response<MutableList<SellingPostItem>>
            ) {
                sellingPostProfileAdapter = context?.let { SellingPostProfileAdapter(it, response.body() as MutableList<SellingPostItem>) }
                recyclerView?.adapter = sellingPostProfileAdapter
                sellingPostProfileAdapter?.notifyDataSetChanged()
                refreshProduct()
                if(progressBarTabProfile != null) {
                    progressBarTabProfile.visibility = View.GONE
                } else {
                    progressBarTabProfile.visibility = View.VISIBLE
                }

            }

            override fun onFailure(call: Call<MutableList<SellingPostItem>>, t: Throwable) {
                Log.e("tag", t.toString())
            }
        })
    }

    fun getCampaignListData(){

        val sharedPref = PreferenceHelper(requireActivity())

        RetrofitInterface().campaignPostProfile(
            sharedPref.getInt(Constant.PREF_ID)!!
        ).enqueue(object : Callback<MutableList<CampaignModel>> {

            override fun onResponse(
                call: Call<MutableList<CampaignModel>>,
                response: Response<MutableList<CampaignModel>>
            ) {
                campaignListProfileAdapter = context?.let { CampaignListProfileAdapter(it, response.body() as MutableList<CampaignModel>) }
                recyclerView?.adapter = campaignListProfileAdapter
                campaignListProfileAdapter?.notifyDataSetChanged()
                refreshCampaign()
                if(progressBarTabProfile != null) {
                    progressBarTabProfile.visibility = View.GONE
                } else {
                    progressBarTabProfile.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<MutableList<CampaignModel>>, t: Throwable) {
                Log.e("tag", t.toString())
            }
        })
    }

}



