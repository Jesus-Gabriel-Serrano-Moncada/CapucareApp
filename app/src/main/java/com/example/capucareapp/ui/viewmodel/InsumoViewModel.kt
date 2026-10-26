package com.example.capucareapp.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.capucareapp.data.model.Insumo
import com.example.capucareapp.data.repository.InsumoRepository

class InsumoViewModel(private val repository: InsumoRepository) : ViewModel() {

    val listaInsumos = mutableStateListOf<Insumo>()

    init {
        cargarInventario()
    }

    fun cargarInventario() {
        listaInsumos.clear()
        listaInsumos.addAll(repository.obtenerInventario())
    }

    fun registrarNuevoInsumo(
        id: String,
        nombre: String,
        descripcion: String,
        categoria: String,
        cantidad: String,
        unidad: String,
        minimo: String,
        ubicacion: String,
        estado: String
    ) {
        val insumo = Insumo(
            id = id,
            nombre = nombre,
            descripcion = descripcion,
            categoria = categoria,
            cantidad = cantidad.toIntOrNull() ?: 0,
            unidad = unidad,
            minimo = minimo.toIntOrNull() ?: 0,
            ubicacion = ubicacion,
            estado = estado
        )
        repository.insertarInsumo(insumo)
        cargarInventario() // Actualiza la lista automáticamente al guardar
    }
}