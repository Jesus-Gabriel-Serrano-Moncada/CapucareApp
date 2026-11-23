package com.example.capucareapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.example.capucareapp.R
import com.example.capucareapp.data.model.Insumo
import com.example.capucareapp.ui.viewmodel.InsumoViewModel

@Composable
fun DetalleInsumoScreen(
    insumo: Insumo,
    viewModel: InsumoViewModel,
    onVolver: () -> Unit
) {
    var tipoMovimiento by remember { mutableStateOf("ENTRADA") }
    var cantidad by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var movimientoExitoso by remember { mutableStateOf(false) }

    val insumoActual = viewModel.listaInsumos.find {
        it.id == insumo.id
    } ?: insumo

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text(stringResource(R.string.detalle_volver))
        }

        Text(
            text = stringResource(R.string.detalle_titulo),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!insumoActual.imagenUrl.isNullOrBlank()) {
            SubcomposeAsyncImage(
                model = insumoActual.imagenUrl,
                contentDescription = stringResource(R.string.detalle_imagen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop,
                loading = {
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.CircularProgressIndicator()
                    }
                },
                error = {
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.detalle_error_imagen))
                    }
                },
                success = {
                    SubcomposeAsyncImageContent()
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = insumoActual.nombre,
            style = MaterialTheme.typography.titleLarge
        )

        Text(stringResource(R.string.detalle_codigo, insumoActual.id))
        Text(stringResource(R.string.detalle_categoria, insumoActual.categoria))
        Text(stringResource(R.string.detalle_descripcion, insumoActual.descripcion))
        Text(stringResource(R.string.detalle_ubicacion, insumoActual.ubicacion))
        Text(stringResource(R.string.detalle_estado, insumoActual.estado))
        Text(stringResource(R.string.detalle_unidad, insumoActual.unidad))

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.detalle_stock),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = stringResource(
                        R.string.detalle_stock_cantidad,
                        insumoActual.cantidad,
                        insumoActual.unidad
                    ),
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    stringResource(
                        R.string.detalle_stock_minimo,
                        insumoActual.minimo
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.detalle_gestionar),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilterChip(
                selected = tipoMovimiento == "ENTRADA",
                onClick = {
                    tipoMovimiento = "ENTRADA"
                    mensaje = ""
                },
                label = {
                    Text(stringResource(R.string.detalle_entrada))
                }
            )

            FilterChip(
                selected = tipoMovimiento == "SALIDA",
                onClick = {
                    tipoMovimiento = "SALIDA"
                    mensaje = ""
                },
                label = {
                    Text(stringResource(R.string.detalle_salida))
                }
            )
        }

        OutlinedTextField(
            value = cantidad,
            onValueChange = {
                cantidad = it.filter { caracter -> caracter.isDigit() }
                mensaje = ""
            },
            label = {
                Text(stringResource(R.string.detalle_cantidad))
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = motivo,
            onValueChange = {
                motivo = it
                mensaje = ""
            },
            label = {
                Text(stringResource(R.string.detalle_motivo))
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val cantidadNumerica = cantidad.toIntOrNull()

                when {
                    cantidadNumerica == null || cantidadNumerica <= 0 -> {
                        mensaje = "cantidad_error"
                        movimientoExitoso = false
                    }

                    motivo.isBlank() -> {
                        mensaje = "motivo_error"
                        movimientoExitoso = false
                    }

                    else -> {
                        val registrado = viewModel.registrarMovimiento(
                            insumoId = insumoActual.id,
                            tipo = tipoMovimiento,
                            cantidad = cantidadNumerica,
                            motivo = motivo.trim(),
                            perfilResponsable = "Usuario"
                        )

                        if (registrado) {
                            mensaje = "exito"
                            movimientoExitoso = true
                            cantidad = ""
                            motivo = ""
                        } else {
                            mensaje = if (tipoMovimiento == "SALIDA") {
                                "stock_insuficiente"
                            } else {
                                "error_registro"
                            }
                            movimientoExitoso = false
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.detalle_confirmar))
        }

        if (mensaje.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))

            val textoMensaje = when (mensaje) {
                "cantidad_error" -> stringResource(R.string.detalle_cantidad_error)
                "motivo_error" -> stringResource(R.string.detalle_motivo_error)
                "exito" -> stringResource(R.string.detalle_exito)
                "stock_insuficiente" -> stringResource(R.string.detalle_stock_insuficiente)
                else -> stringResource(R.string.detalle_error_registro)
            }

            Text(
                text = textoMensaje,
                color = if (movimientoExitoso) {
                    Color(0xFF2E7D32)
                } else {
                    MaterialTheme.colorScheme.error
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
