package com.example.capucareapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capucareapp.data.model.Insumo
import com.example.capucareapp.data.repository.InsumoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InsumoViewModel(private val repository: InsumoRepository) : ViewModel() {

    // Manejo de estado para la interfaz gráfica
    private val _estadoValidacion = MutableStateFlow<String?>(null)
    val estadoValidacion: StateFlow<String?> = _estadoValidacion

    // Lógica de validación centralizada y desacoplada[cite: 5]
    fun registrarNuevoInsumo(
        id: String, nombre: String, categoria: String, cantidad: String,
        unidad: String, minimo: String, ubicacion: String
    ) {
        if (id.isBlank() || nombre.isBlank() || cantidad.isBlank()) {
            _estadoValidacion.value = "Error: ID, Nombre y Cantidad son campos obligatorios."
            return
        }

        val cantidadInt = cantidad.toIntOrNull()
        val minimoInt = minimo.toIntOrNull() ?: 0

        if (cantidadInt == null || cantidadInt < 0) {
            _estadoValidacion.value = "Error: La cantidad debe ser un número válido mayor a cero."
            return
        }

        // Si pasa las validaciones, creamos el objeto
        val nuevoInsumo = Insumo(
            idProducto = id,
            nombreDescripcion = nombre,
            categoria = categoria,
            cantidadDisponible = cantidadInt,
            unidadMedida = unidad,
            nivelMinimo = minimoInt,
            ubicacion = ubicacion,
            estado = "Activo"
        )

        // Guardamos en la base de datos de forma asíncrona
        viewModelScope.launch {
            repository.insertarInsumo(nuevoInsumo)
            _estadoValidacion.value = "¡Insumo médico registrado correctamente!"
        }
    }
}

