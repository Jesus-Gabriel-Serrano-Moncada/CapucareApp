package com.example.capucareapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.capucareapp.data.local.AppDatabase
import com.example.capucareapp.data.network.MindicadorRetrofitClient
import com.example.capucareapp.data.repository.IndicadoresRepository
import com.example.capucareapp.data.repository.InsumoRepository
import com.example.capucareapp.ui.screens.MainContainerScreen
import com.example.capucareapp.ui.viewmodel.IndicadoresViewModel
import com.example.capucareapp.ui.viewmodel.InsumoViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val insumoRepository = InsumoRepository(database.insumoDao())

        val indicadoresRepository = IndicadoresRepository(
            MindicadorRetrofitClient.api
        )

        setContent {
            val insumoFactory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return InsumoViewModel(insumoRepository) as T
                }
            }

            val indicadoresFactory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return IndicadoresViewModel(indicadoresRepository) as T
                }
            }

            val insumoViewModel: InsumoViewModel =
                viewModel(factory = insumoFactory)

            val indicadoresViewModel: IndicadoresViewModel =
                viewModel(factory = indicadoresFactory)

            MainContainerScreen(
                viewModel = insumoViewModel,
                indicadoresViewModel = indicadoresViewModel
            )
        }
    }
}
