package com.example.capucareapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.capucareapp.data.model.Insumo
import com.example.capucareapp.ui.viewmodel.InsumoViewModel

@Composable
fun MainContainerScreen(viewModel: InsumoViewModel) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Registro / Escáner", "Consulta Existencias")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = {
                        if (index == 1) viewModel.cargarInventario()
                        tabIndex = index
                    }
                )
            }
        }

        when (tabIndex) {
            0 -> RegistroInsumoScreen(viewModel = viewModel)
            1 -> ConsultaInventarioScreen(viewModel = viewModel)
        }
    }
}

@Composable
fun ConsultaInventarioScreen(viewModel: InsumoViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Inventario de Insumos Médicos",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (viewModel.listaInsumos.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay insumos registrados en la base de datos.")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(viewModel.listaInsumos) { insumo ->
                    ItemInsumoCard(insumo = insumo)
                }
            }
        }
    }
}

@Composable
fun ItemInsumoCard(insumo: Insumo) {
    val esCritico = insumo.cantidad <= insumo.minimo

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (esCritico) Color(0xFFFFEBEE) else Color(0xFFF5F5F5)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = insumo.nombre, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = if (esCritico) "STOCK CRÍTICO" else insumo.estado,
                    color = if (esCritico) Color.Red else Color.DarkGray,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Código: ${insumo.id} | Ubicación: ${insumo.ubicacion}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Descripción: ${insumo.descripcion}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Disponible: ${insumo.cantidad} ${insumo.unidad} (Mínimo: ${insumo.minimo})",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

