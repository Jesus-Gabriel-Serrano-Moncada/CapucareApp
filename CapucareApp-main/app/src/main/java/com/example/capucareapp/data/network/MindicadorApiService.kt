package com.example.capucareapp.data.network

import com.example.capucareapp.data.model.IndicadoresResponse
import retrofit2.http.GET

interface MindicadorApiService {

    @GET("api")
    suspend fun obtenerIndicadores(): IndicadoresResponse
}

