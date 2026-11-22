package com.example.capucareapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capucareapp.data.model.IndicadoresResponse
import com.example.capucareapp.data.repository.IndicadoresRepository
import kotlinx.coroutines.launch

data class IndicadoresUiState(
    val cargando: Boolean = false,
    val indicadores: IndicadoresResponse? = null,
    val error: String? = null
)

class IndicadoresViewModel(
    private val repository: IndicadoresRepository
) : ViewModel() {

    var estado by mutableStateOf(IndicadoresUiState())
        private set

    init {
        cargarIndicadores()
    }

    fun cargarIndicadores() {
        viewModelScope.launch {
            estado = estado.copy(
                cargando = true,
                error = null
            )

            try {
                val respuesta = repository.obtenerIndicadores()

                estado = IndicadoresUiState(
                    cargando = false,
                    indicadores = respuesta
                )
            } catch (e: Exception) {
                estado = estado.copy(
                    cargando = false,
                    error = "No fue posible obtener los indicadores económicos."
                )
            }
        }
    }
}


