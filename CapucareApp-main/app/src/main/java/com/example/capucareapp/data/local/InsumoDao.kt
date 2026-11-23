package com.example.capucareapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.capucareapp.data.model.Insumo
import com.example.capucareapp.data.model.Movimiento

@Dao
interface InsumoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarInsumo(insumo: Insumo)

    @Query("SELECT * FROM insumos")
    fun obtenerTodosLosInsumos(): List<Insumo>

    @Query("SELECT * FROM insumos WHERE id = :id LIMIT 1")
    fun obtenerInsumoPorId(id: String): Insumo?

    @Query("UPDATE insumos SET cantidad = :cantidad WHERE id = :id")
    fun actualizarCantidad(id: String, cantidad: Int)

    @Insert
    fun registrarMovimiento(movimiento: Movimiento)

    @Query("SELECT * FROM movimientos ORDER BY fecha DESC")
    fun obtenerTodosLosMovimientos(): List<Movimiento>

    @Query("""
        SELECT * FROM movimientos
        WHERE insumoId = :idProducto
        ORDER BY fecha DESC
    """)
    fun obtenerHistorialPorProducto(idProducto: String): List<Movimiento>

    @Transaction
    fun aplicarMovimiento(movimiento: Movimiento): Boolean {
        val insumo = obtenerInsumoPorId(movimiento.insumoId)
            ?: return false

        if (movimiento.cantidad <= 0) {
            return false
        }

        val nuevaCantidad = when (movimiento.tipo.uppercase()) {
            "ENTRADA" -> insumo.cantidad + movimiento.cantidad

            "SALIDA" -> {
                if (insumo.cantidad < movimiento.cantidad) {
                    return false
                }

                insumo.cantidad - movimiento.cantidad
            }

            else -> return false
        }

        actualizarCantidad(insumo.id, nuevaCantidad)
        registrarMovimiento(movimiento)

        return true
    }
}
