package com.example.appbannoithat.Server

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBanNoiThat {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.206:3000/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val server: Server by lazy {
        retrofit.create(Server::class.java)
    }
}