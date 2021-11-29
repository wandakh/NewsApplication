package com.wanda.idn.newsapplication.service

import com.google.gson.GsonBuilder
import com.wanda.idn.newsapplication.service.Const.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Time
import java.util.concurrent.TimeUnit

object RetrofitConfig {
    val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val client = OkHttpClient.Builder().addInterceptor(interceptor)
        .retryOnConnectionFailure(true)
        .connectTimeout(20, TimeUnit.SECONDS).build()

    val gson = GsonBuilder().setLenient().create()
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .client(client).addConverterFactory(GsonConverterFactory.create(gson)).build()


    fun getiInstance(): ApiService = retrofit.create(ApiService::class.java)

}