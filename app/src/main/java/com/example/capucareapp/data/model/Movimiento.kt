package com.example.capucareapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movimientos")
data class Movimiento(
    @PrimaryKey(autoGenerate = true) val idMovimiento: Int = 0,
    val insumoId: String,
    val fecha: Long = System.currentTimeMillis(),
    val tipo: String,
    val cantidad: Int,
    val motivo: String,
    val perfilResponsable: String
)