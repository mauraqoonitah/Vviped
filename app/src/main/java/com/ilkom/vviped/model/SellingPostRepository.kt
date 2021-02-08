package com.ilkom.vviped.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SellingPostRepository {
    fun create(): SellingPostService{
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://vviped.com/")
            .build()

        return retrofit.create(SellingPostService::class.java)
    }
}
