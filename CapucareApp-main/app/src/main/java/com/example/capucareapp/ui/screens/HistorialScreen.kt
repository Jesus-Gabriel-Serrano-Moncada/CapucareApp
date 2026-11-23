package com.example.capucareapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.capucareapp.R
import com.example.capucareapp.data.model.Insumo
import com.example.capucareapp.data.model.Movimiento
import com.example.capucareapp.ui.viewmodel.InsumoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistorialScreen(viewModel: InsumoViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.historial_titulo),
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = stringResource(R.string.historial_subtitulo),
            style = MaterialTheme.typography.bodyMedium
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (viewModel.listaMovimientos.isEmpty()) {
                item {
                    Text(stringResource(R.string.historial_vacio))
                }
            } else {
                items(
                    items = viewModel.listaMovimientos,
                    key = { it.idMovimiento }
                ) { movimiento ->
                    MovimientoCard(
                        movimiento = movimiento,
                        insumos = viewModel.listaInsumos
                    )
                }
            }
        }
    }
}

@Composable
fun MovimientoCard(
    movimiento: Movimiento,
    insumos: List<Insumo>
) {
    val nombreInsumo = insumos.find {
        it.id == movimiento.insumoId
    }?.nombre ?: movimiento.insumoId

    val esEntrada = movimiento.tipo.uppercase() == "ENTRADA"

    val fechaFormateada = remember(movimiento.fecha) {
        SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        ).format(Date(movimiento.fecha))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = nombreInsumo,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = if (esEntrada) {
                    stringResource(R.string.historial_entrada, movimiento.cantidad)
                } else {
                    stringResource(R.string.historial_salida, movimiento.cantidad)
                },
                color = if (esEntrada) {
                    Color(0xFF2E7D32)
                } else {
                    MaterialTheme.colorScheme.error
                },
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                stringResource(
                    R.string.historial_motivo,
                    movimiento.motivo
                )
            )

            Text(
                stringResource(
                    R.string.historial_responsable,
                    movimiento.perfilResponsable
                )
            )

            Text(
                stringResource(
                    R.string.historial_fecha,
                    fechaFormateada
                )
            )
        }
    }
}
