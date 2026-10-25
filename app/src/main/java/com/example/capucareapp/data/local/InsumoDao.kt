package com.example.capucareapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.capucareapp.data.model.Insumo
import com.example.capucareapp.data.model.Movimiento

@Dao
interface InsumoDao {

    // 1. Ingreso de nuevos productos o actualización de stock
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarInsumo(insumo: Insumo)

    // 2. Consulta de existencias para la vista principal
    @Query("SELECT * FROM tabla_insumos")
    suspend fun obtenerTodosLosInsumos(): List<Insumo>

    // 3. Registrar un movimiento (Ingreso o Salida/Venta)
    @Insert
    suspend fun registrarMovimiento(movimiento: Movimiento)

    // 4. Obtener historial de movimientos exigido en el encargo
    @Query("SELECT * FROM tabla_movimientos WHERE idProductoRelacionado = :idProducto")
    suspend fun obtenerHistorialPorProducto(idProducto: String): List<Movimiento>
}