package com.wanda.idn.newsapplication.service

import com.wanda.idn.newsapplication.model.ResponseNews
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    fun getNewsHeadLine(
        @Query("country")country:String?,
        @Query("apiKey")apiKey:String?
    ):Call<ResponseNews>

}