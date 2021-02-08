package com.ilkom.vviped.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilkom.vviped.*
import com.ilkom.vviped.model.*
import com.ilkom.vviped.model.login.Constant
import com.ilkom.vviped.model.login.PreferenceHelper
import kotlinx.android.synthetic.main.fragment_campaign_list.*
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CampaignListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CampaignListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var recyclerView: RecyclerView? = null
    private var campaignListAdapter: CampaignListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_campaign_list, container, false)

        recyclerView = view.findViewById(R.id.recycleView_campaign)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        getData()
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_create_campaign.setOnClickListener{
            val intent = Intent(activity, create_campaign::class.java)
            startActivity(intent)

            val sharedPref = PreferenceHelper(requireActivity())
            RetrofitInterface().userActivities(
                sharedPref.getInt(Constant.PREF_ID)!!,
                sharedPref.getString(Constant.PREF_USERNAME)!!,
                RequestBody.create(MediaType.parse("multipart/form-data"), "Click add new campaign button"),
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

    fun getData(){
        val campaignService = CampaignRepository.create()
        campaignService.getCampaigns().enqueue(object : Callback<MutableList<CampaignModel>> {
            override fun onResponse(
                call: Call<MutableList<CampaignModel>>,
                response: Response<MutableList<CampaignModel>>
            ) {
                campaignListAdapter = context?.let { CampaignListAdapter(it, response.body() as MutableList<CampaignModel>) }
                recyclerView?.adapter = campaignListAdapter
                campaignListAdapter?.notifyDataSetChanged()
                refreshTabCampaign()
                if(progressBarTabCampign != null) {
                    progressBarTabCampign.visibility = View.GONE
                } else {
                    progressBarTabCampign.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<MutableList<CampaignModel>>, t: Throwable) {
                Log.e("tag", t.toString())
            }
        })
    }

    private fun refreshTabCampaign() {
        swipeRefreshTabCampaign.setOnRefreshListener {
            getData()
            swipeRefreshTabCampaign.isRefreshing = false
        }
    }
}
