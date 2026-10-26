package com.example.capucareapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.capucareapp.NotificationHelper
import com.example.capucareapp.ui.viewmodel.InsumoViewModel

@Composable
fun RegistroInsumoScreen(viewModel: InsumoViewModel) {
    val context = LocalContext.current
    var id by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var minimo by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("Activo") }

    val scanner = remember { com.google.mlkit.vision.codescanner.GmsBarcodeScanning.getClient(context) }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted -> }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = {
            scanner.startScan().addOnSuccessListener { barcode ->
                id = barcode.rawValue ?: ""
            }
        }) {
            Text("Escanear Código")
        }
        OutlinedTextField(value = id, onValueChange = { id = it }, label = { Text("Código") })
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
        OutlinedTextField(value = cantidad, onValueChange = { cantidad = it }, label = { Text("Cantidad") })
        OutlinedTextField(value = minimo, onValueChange = { minimo = it }, label = { Text("Mínimo") })
        OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("Estado (Ej: Activo)") })

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                viewModel.registrarNuevoInsumo(id, nombre, descripcion, "General", cantidad, "Unidades", minimo, "Almacén", estado)
                val cant = cantidad.toIntOrNull() ?: 0
                val min = minimo.toIntOrNull() ?: 0
                if (cant <= min) {
                    NotificationHelper(context).mostrarNotificacion("Stock Crítico", "El insumo $nombre alcanzó el mínimo.")
                }
            }) { Text("Guardar Insumo") }
    }
}