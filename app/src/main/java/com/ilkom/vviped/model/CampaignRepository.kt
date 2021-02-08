package com.ilkom.vviped.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CampaignRepository {
    fun create(): CampaignService{
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://jakartaqurban.com/")
            .build()

        return retrofit.create(CampaignService::class.java)
    }
}