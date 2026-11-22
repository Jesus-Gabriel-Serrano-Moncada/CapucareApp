package com.example.capucareapp.data.model

import com.google.gson.annotations.SerializedName

data class IndicadoresResponse(
    val uf: IndicadorEconomico? = null,
    val dolar: IndicadorEconomico? = null,
    val euro: IndicadorEconomico? = null,
    val utm: IndicadorEconomico? = null,
    val ipc: IndicadorEconomico? = null
)

data class IndicadorEconomico(
    val nombre: String = "",

    @SerializedName("unidad_medida")
    val unidadMedida: String = "",

    val fecha: String = "",
    val valor: Double = 0.0
)


