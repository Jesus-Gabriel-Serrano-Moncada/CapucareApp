package com.example.capucareapp.data.repository

import com.example.capucareapp.data.model.IndicadoresResponse
import com.example.capucareapp.data.network.MindicadorApiService

class IndicadoresRepository(
    private val api: MindicadorApiService
) {

    suspend fun obtenerIndicadores(): IndicadoresResponse {
        return api.obtenerIndicadores()
    }
}
