package com.example.capucareapp.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capucareapp.ui.viewmodel.InsumoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroInsumoScreen(viewModel: InsumoViewModel) {
    val estadoValidacion by viewModel.estadoValidacion.collectAsState()

    var id by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var unidad by remember { mutableStateOf("") }
    var minimo by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }

    // Estado para guardar la foto capturada
    var fotoBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Lanzador nativo de la cámara del dispositivo
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        fotoBitmap = bitmap // Guardamos la foto si el usuario tomó una
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ingreso de Insumo Médico",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AnimatedVisibility(visible = estadoValidacion != null, enter = fadeIn(), exit = fadeOut()) {
            val isError = estadoValidacion?.startsWith("Error") == true
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isError) MaterialTheme.colorScheme.errorContainer else Color(0xFFD1E7DD)
                ),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text(
                    text = estadoValidacion ?: "",
                    color = if (isError) MaterialTheme.colorScheme.onErrorContainer else Color(0xFF0F5132),
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // --- SECCIÓN DE LA CÁMARA ---
        Button(
            onClick = { cameraLauncher.launch(null) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Icon(Icons.Default.AddCircle, contentDescription = "Cámara", modifier = Modifier.padding(end = 8.dp))
            Text("Tomar foto del producto")
        }

        // Si hay una foto, la mostramos con bordes redondeados
        fotoBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Foto del insumo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
        // -----------------------------

        OutlinedTextField(
            value = id, onValueChange = { id = it }, label = { Text("Código/ID") },
            leadingIcon = { Icon(Icons.Default.Info, contentDescription = "ID Icon") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre y Descripción") },
            leadingIcon = { Icon(Icons.Default.Create, contentDescription = "Nombre Icon") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = cantidad, onValueChange = { cantidad = it }, label = { Text("Cantidad Disponible") },
            leadingIcon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cantidad Icon") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Button(
            onClick = { viewModel.registrarNuevoInsumo(id, nombre, categoria, cantidad, unidad, minimo, ubicacion) },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Registrar Insumo")
        }
    }
}