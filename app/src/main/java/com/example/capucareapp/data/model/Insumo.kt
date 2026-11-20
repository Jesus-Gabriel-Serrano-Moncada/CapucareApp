package com.example.capucareapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "insumos")
data class Insumo(
    @PrimaryKey val id: String,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val cantidad: Int,
    val unidad: String,
    val minimo: Int,
    val ubicacion: String,
    val estado: String,
    val imagenUrl: String? = null
)
