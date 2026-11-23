package com.example.capucareapp.data.repository

import com.example.capucareapp.data.local.InsumoDao
import com.example.capucareapp.data.model.Insumo
import com.example.capucareapp.data.model.Movimiento

class InsumoRepository(private val insumoDao: InsumoDao) {

    fun insertarInsumo(insumo: Insumo) {
        insumoDao.insertarInsumo(insumo)
    }

    fun obtenerInventario(): List<Insumo> {
        return insumoDao.obtenerTodosLosInsumos()
    }

    fun obtenerHistorial(): List<Movimiento> {
        return insumoDao.obtenerTodosLosMovimientos()
    }

    fun registrarMovimiento(movimiento: Movimiento): Boolean {
        return insumoDao.aplicarMovimiento(movimiento)
    }
}
