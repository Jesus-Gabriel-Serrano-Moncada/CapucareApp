package com.example.capucareapp.data.repository

import com.example.capucareapp.data.local.InsumoDao
import com.example.capucareapp.data.model.Insumo

class InsumoRepository(private val insumoDao: InsumoDao) {

    suspend fun insertarInsumo(insumo: Insumo) {
        insumoDao.insertarInsumo(insumo)
    }

    suspend fun obtenerInventario(): List<Insumo> {
        return insumoDao.obtenerTodosLosInsumos()
    }
}

