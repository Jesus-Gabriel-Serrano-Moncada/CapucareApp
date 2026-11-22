
package com.example.capucareapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.capucareapp.R
import com.example.capucareapp.data.model.IndicadorEconomico
import com.example.capucareapp.ui.viewmodel.IndicadoresViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun IndicadoresScreen(viewModel: IndicadoresViewModel) {
    val estado = viewModel.estado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.indicadores_titulo),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.indicadores_subtitulo),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        when {
            estado.cargando -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(stringResource(R.string.indicadores_cargando))
                    }
                }
            }

            estado.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.indicadores_error),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.cargarIndicadores() }
                        ) {
                            Text(stringResource(R.string.indicadores_reintentar))
                        }
                    }
                }
            }

            estado.indicadores != null -> {
                val indicadores = estado.indicadores

                val listaIndicadores = listOfNotNull(
                    indicadores.uf,
                    indicadores.dolar,
                    indicadores.euro,
                    indicadores.utm,
                    indicadores.ipc
                )

                if (listaIndicadores.isEmpty()) {
                    Text(stringResource(R.string.indicadores_vacios))
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(listaIndicadores) { indicador ->
                            IndicadorCard(indicador)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IndicadorCard(indicador: IndicadorEconomico) {
    val formatoNumero = NumberFormat.getNumberInstance(
        Locale("es", "CL")
    ).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    val valorFormateado = when (indicador.unidadMedida.lowercase()) {
        "porcentaje" -> "${formatoNumero.format(indicador.valor)} %"
        else -> "$${formatoNumero.format(indicador.valor)}"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = indicador.nombre,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = valorFormateado,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(
                    R.string.indicadores_unidad,
                    indicador.unidadMedida
                ),
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = stringResource(
                    R.string.indicadores_fecha,
                    indicador.fecha.substringBefore("T")
                ),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
