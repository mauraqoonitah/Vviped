package com.ilkom.vviped.model
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SellingPostService {
    @GET("api.php?apicall=sellingproducts")
    fun getFeeds(): Call<MutableList<SellingPostItem>>

    @GET("detect")
    fun detectedObject(@Query("link") objectName: String): Call<List<DetectedItem>>


}

