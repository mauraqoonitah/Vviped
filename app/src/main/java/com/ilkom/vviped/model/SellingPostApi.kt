package com.ilkom.vviped.model

import retrofit2.http.GET
import retrofit2.http.Query

interface SellingPostApi {
    companion object{
        const val BASE_URL = "http://127.0.0.1:5000/"
    }
    @GET("SearchProduct")
    suspend fun searchProduct(
        @Query("query") query: String.Companion = String,
        @Query("page") page: Int.Companion = Int,
        @Query("per_page") perPage: Int.Companion = Int
    )
}
