package com.example.capucareapp.data.repository

import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getBackendData(): Flow<Result<List<Any>>>
    suspend fun sendDataToBackend(data: Any): Result<Boolean>

    fun getExternalApiData(): Flow<Result<List<Any>>>
}