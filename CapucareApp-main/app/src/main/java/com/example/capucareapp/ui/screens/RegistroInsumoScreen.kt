package com.example.capucareapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.core.content.ContextCompat
import coil3.compose.AsyncImage
import com.example.capucareapp.NotificationHelper
import com.example.capucareapp.R
import com.example.capucareapp.ui.viewmodel.InsumoViewModel
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

@Composable
fun RegistroInsumoScreen(viewModel: InsumoViewModel) {
    val context = LocalContext.current

    var id by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var minimo by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("ACTIVO") }
    var estadoExpandido by remember { mutableStateOf(false) }
    var imagenUrl by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }
    var mensajeExito by remember { mutableStateOf("") }

    val scanner = GmsBarcodeScanning.getClient(context)

    val notificationHelper = remember(context) {
        NotificationHelper(context)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    val errorCodigo = stringResource(R.string.error_codigo_obligatorio)
    val errorNombre = stringResource(R.string.error_nombre_obligatorio)
    val errorDuplicado = stringResource(R.string.error_codigo_duplicado)
    val errorCantidad = stringResource(R.string.error_cantidad)
    val errorMinimo = stringResource(R.string.error_minimo)
    val errorUrl = stringResource(R.string.error_url_imagen)
    val registroExitoso = stringResource(R.string.registro_exitoso)
    val tituloNotificacion = stringResource(R.string.notificacion_stock_titulo)
    val mensajeNotificacion = stringResource(
        R.string.notificacion_stock_mensaje,
        nombre
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(R.string.registro_titulo),
            style = MaterialTheme.typography.headlineMedium
        )

        Button(
            onClick = {
                scanner.startScan()
                    .addOnSuccessListener { barcode ->
                        barcode.rawValue?.let {
                            id = it
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.escanear_codigo))
        }

        OutlinedTextField(
            value = id,
            onValueChange = { id = it },
            label = { Text(stringResource(R.string.codigo_insumo)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(stringResource(R.string.nombre)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text(stringResource(R.string.descripcion)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = cantidad,
            onValueChange = { cantidad = it.filter(Char::isDigit) },
            label = { Text(stringResource(R.string.cantidad)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = minimo,
            onValueChange = { minimo = it.filter(Char::isDigit) },
            label = { Text(stringResource(R.string.stock_minimo)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Selector de estado
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = if (estado == "ACTIVO") {
                    stringResource(R.string.estado_activo)
                } else {
                    stringResource(R.string.estado_inactivo)
                },
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = {
                    Text(stringResource(R.string.estado))
                },
                trailingIcon = {
                    Text("▼")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable {
                        estadoExpandido = true
                    }
            )

            DropdownMenu(
                expanded = estadoExpandido,
                onDismissRequest = {
                    estadoExpandido = false
                }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(stringResource(R.string.estado_activo))
                    },
                    onClick = {
                        estado = "ACTIVO"
                        estadoExpandido = false
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(stringResource(R.string.estado_inactivo))
                    },
                    onClick = {
                        estado = "INACTIVO"
                        estadoExpandido = false
                    }
                )
            }
        }

        HorizontalDivider()

        Text(
            text = stringResource(R.string.imagen_insumo),
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = imagenUrl,
            onValueChange = { imagenUrl = it },
            label = { Text(stringResource(R.string.url_imagen)) },
            placeholder = { Text("https://ejemplo.com/imagen.jpg") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (
            imagenUrl.startsWith("https://") ||
            imagenUrl.startsWith("http://")
        ) {
            AsyncImage(
                model = imagenUrl,
                contentDescription = stringResource(R.string.imagen_insumo),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
        }

        if (mensajeError.isNotEmpty()) {
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (mensajeExito.isNotEmpty()) {
            Text(
                text = mensajeExito,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Button(
            onClick = {
                mensajeError = ""
                mensajeExito = ""

                when {
                    id.isBlank() ->
                        mensajeError = errorCodigo

                    nombre.isBlank() ->
                        mensajeError = errorNombre

                    viewModel.listaInsumos.any {
                        it.id == id.trim()
                    } ->
                        mensajeError = errorDuplicado

                    cantidad.toIntOrNull() == null ->
                        mensajeError = errorCantidad

                    minimo.toIntOrNull() == null ->
                        mensajeError = errorMinimo

                    imagenUrl.isNotBlank() &&
                            !imagenUrl.startsWith("https://") &&
                            !imagenUrl.startsWith("http://") ->
                        mensajeError = errorUrl

                    else -> {
                        val cant = cantidad.toInt()
                        val min = minimo.toInt()

                        viewModel.registrarNuevoInsumo(
                            id.trim(),
                            nombre.trim(),
                            descripcion.trim(),
                            "General",
                            cantidad,
                            "Unidades",
                            minimo,
                            "Almacén",
                            estado,
                            imagenUrl.trim().takeIf { it.isNotBlank() }
                        )

                        mensajeExito = registroExitoso

                        if (cant <= min) {
                            notificationHelper.mostrarNotificacion(
                                tituloNotificacion,
                                mensajeNotificacion
                            )
                        }

                        id = ""
                        nombre = ""
                        descripcion = ""
                        cantidad = ""
                        minimo = ""
                        estado = "ACTIVO"
                        imagenUrl = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.guardar_insumo))
        }
    }
}
