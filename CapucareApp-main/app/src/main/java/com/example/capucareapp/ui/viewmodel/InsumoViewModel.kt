package com.example.capucareapp.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.capucareapp.data.model.Insumo
import com.example.capucareapp.data.model.Movimiento
import com.example.capucareapp.data.repository.InsumoRepository

class InsumoViewModel(private val repository: InsumoRepository) : ViewModel() {

    val listaInsumos = mutableStateListOf<Insumo>()
    val listaMovimientos = mutableStateListOf<Movimiento>()

    init {
        cargarInventario()
        cargarHistorial()
    }

    fun cargarInventario() {
        listaInsumos.clear()
        listaInsumos.addAll(repository.obtenerInventario())
    }

    fun cargarHistorial() {
        listaMovimientos.clear()
        listaMovimientos.addAll(repository.obtenerHistorial())
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
        estado: String,
        imagenUrl: String? = null
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
            estado = estado,
            imagenUrl = imagenUrl
        )

        repository.insertarInsumo(insumo)
        cargarInventario()
    }

    fun registrarMovimiento(
        insumoId: String,
        tipo: String,
        cantidad: Int,
        motivo: String,
        perfilResponsable: String
    ): Boolean {
        if (cantidad <= 0) {
            return false
        }

        val movimiento = Movimiento(
            insumoId = insumoId,
            tipo = tipo,
            cantidad = cantidad,
            motivo = motivo,
            perfilResponsable = perfilResponsable
        )

        val registrado = repository.registrarMovimiento(movimiento)

        if (registrado) {
            cargarInventario()
            cargarHistorial()
        }

        return registrado
    }
}
