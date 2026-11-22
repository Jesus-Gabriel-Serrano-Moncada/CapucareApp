package com.example.capucareapp.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MindicadorRetrofitClient {

    private const val BASE_URL = "https://mindicador.cl/"

    val api: MindicadorApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MindicadorApiService::class.java)
    }
}
