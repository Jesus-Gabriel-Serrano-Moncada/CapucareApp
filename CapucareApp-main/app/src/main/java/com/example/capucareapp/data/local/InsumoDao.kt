package com.example.capucareapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.capucareapp.data.model.Insumo
import com.example.capucareapp.data.model.Movimiento

@Dao
interface InsumoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarInsumo(insumo: Insumo)

    @Query("SELECT * FROM insumos")
    fun obtenerTodosLosInsumos(): List<Insumo>

    @Insert
    fun registrarMovimiento(movimiento: Movimiento)

    @Query("SELECT * FROM movimientos WHERE insumoId = :idProducto")
    fun obtenerHistorialPorProducto(idProducto: String): List<Movimiento>
}