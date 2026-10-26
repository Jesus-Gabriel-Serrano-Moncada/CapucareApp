package com.example.capucareapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "insumos")
data class Insumo(
    @PrimaryKey val id: String, // Código o identificador
    val nombre: String,
    val descripcion: String,    // NUEVO
    val categoria: String,
    val cantidad: Int,
    val unidad: String,
    val minimo: Int,
    val ubicacion: String,
    val estado: String          // NUEVO (Ej: "Activo", "Cuarentena", "Descontinuado")
)