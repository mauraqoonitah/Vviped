package com.ilkom.vviped.model

import retrofit2.Call
import retrofit2.http.GET

interface CampaignService {

    @GET("api.php?apicall=campaigns")
    fun getCampaigns(): Call<MutableList<CampaignModel>>


}