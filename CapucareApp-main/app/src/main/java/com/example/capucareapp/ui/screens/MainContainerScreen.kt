package com.example.capucareapp.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.example.capucareapp.R
import com.example.capucareapp.data.model.Insumo
import com.example.capucareapp.ui.viewmodel.IndicadoresViewModel
import com.example.capucareapp.ui.viewmodel.InsumoViewModel

@Composable
fun MainContainerScreen(
    viewModel: InsumoViewModel,
    indicadoresViewModel: IndicadoresViewModel
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    var insumoSeleccionado by remember { mutableStateOf<Insumo?>(null) }

    val configuracion = LocalConfiguration.current
    val horizontal =
        configuracion.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(
                    if (horizontal) 76.dp else 80.dp
                ),
                windowInsets = WindowInsets(0.dp)
            ) {
                NavigationBarItem(
                    selected = tabIndex == 0,
                    onClick = {
                        tabIndex = 0
                        insumoSeleccionado = null
                    },
                    icon = {
                        Text(
                            text = "＋",
                            fontSize = if (horizontal) 18.sp else 24.sp
                        )
                    },
                    label = {
                        Text(
                            stringResource(R.string.nav_registro),
                            fontSize = if (horizontal) 10.sp else 12.sp
                        )
                    }
                )

                NavigationBarItem(
                    selected = tabIndex == 1,
                    onClick = {
                        viewModel.cargarInventario()
                        tabIndex = 1
                    },
                    icon = {
                        Text(
                            text = "▤",
                            fontSize = if (horizontal) 18.sp else 24.sp
                        )
                    },
                    label = {
                        Text(
                            stringResource(R.string.nav_inventario),
                            fontSize = if (horizontal) 10.sp else 12.sp
                        )
                    }
                )

                NavigationBarItem(
                    selected = tabIndex == 2,
                    onClick = {
                        indicadoresViewModel.cargarIndicadores()
                        tabIndex = 2
                        insumoSeleccionado = null
                    },
                    icon = {
                        Text(
                            text = "$",
                            fontSize = if (horizontal) 18.sp else 24.sp
                        )
                    },
                    label = {
                        Text(
                            stringResource(R.string.nav_indicadores),
                            fontSize = if (horizontal) 10.sp else 12.sp
                        )
                    }
                )

                NavigationBarItem(
                    selected = tabIndex == 3,
                    onClick = {
                        viewModel.cargarInventario()
                        viewModel.cargarHistorial()
                        tabIndex = 3
                        insumoSeleccionado = null
                    },
                    icon = {
                        Text(
                            text = "↔",
                            fontSize = if (horizontal) 18.sp else 24.sp
                        )
                    },
                    label = {
                        Text(
                            stringResource(R.string.nav_historial),
                            fontSize = if (horizontal) 10.sp else 12.sp
                        )
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (tabIndex) {
                0 -> RegistroInsumoScreen(viewModel = viewModel)

                1 -> {
                    if (insumoSeleccionado == null) {
                        ConsultaInventarioScreen(
                            viewModel = viewModel,
                            onSeleccionarInsumo = { insumo ->
                                insumoSeleccionado = insumo
                            }
                        )
                    } else {
                        DetalleInsumoScreen(
                            insumo = insumoSeleccionado!!,
                            viewModel = viewModel,
                            onVolver = {
                                insumoSeleccionado = null
                            }
                        )
                    }
                }

                2 -> IndicadoresScreen(
                    viewModel = indicadoresViewModel
                )

                3 -> HistorialScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun ConsultaInventarioScreen(
    viewModel: InsumoViewModel,
    onSeleccionarInsumo: (Insumo) -> Unit
) {
    val totalInsumos = viewModel.listaInsumos.size

    val insumosCriticos = viewModel.listaInsumos.count {
        it.cantidad <= it.minimo
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.inventario_titulo),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.total_insumos),
                        style = MaterialTheme.typography.labelMedium
                    )

                    Text(
                        text = totalInsumos.toString(),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEBEE)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.stock_critico),
                        style = MaterialTheme.typography.labelMedium
                    )

                    Text(
                        text = insumosCriticos.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Red
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.insumos_registrados),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (viewModel.listaInsumos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.sin_insumos),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(viewModel.listaInsumos) { insumo ->
                    ItemInsumoCard(
                        insumo = insumo,
                        onClick = {
                            onSeleccionarInsumo(insumo)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemInsumoCard(
    insumo: Insumo,
    onClick: () -> Unit = {}
) {
    val esCritico = insumo.cantidad <= insumo.minimo

    val estadoTraducido = when (insumo.estado.trim().uppercase()) {
        "ACTIVO" -> stringResource(R.string.estado_activo)
        "INACTIVO" -> stringResource(R.string.estado_inactivo)
        else -> insumo.estado
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (esCritico) {
                Color(0xFFFFEBEE)
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (!insumo.imagenUrl.isNullOrBlank()) {
                SubcomposeAsyncImage(
                    model = insumo.imagenUrl,
                    contentDescription = stringResource(R.string.imagen_insumo),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(R.string.error_carga_imagen))
                        }
                    },
                    success = {
                        SubcomposeAsyncImageContent()
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.sin_imagen),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = insumo.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = if (esCritico) {
                        stringResource(R.string.stock_critico_estado)
                    } else {
                        estadoTraducido
                    },
                    color = if (esCritico) Color.Red else Color.DarkGray,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.codigo, insumo.id),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(R.string.ubicacion, insumo.ubicacion),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(
                    R.string.descripcion_insumo,
                    insumo.descripcion
                ),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(
                    R.string.disponible,
                    insumo.cantidad,
                    insumo.unidad
                ),
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = stringResource(
                    R.string.stock_minimo_detalle,
                    insumo.minimo
                ),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Toca para ver los detalles",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
