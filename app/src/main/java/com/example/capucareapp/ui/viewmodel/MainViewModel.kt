package com.example.capucareapp.ui.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capucareapp.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.viewModelScope

// Estado de la UI
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<Any>) : UiState()
    data class Error(val message: String) : UiState()
}

class MainViewModel(private val repository: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun fetchBackendData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getBackendData().collect { result ->
                result.fold(
                    onSuccess = { data -> _uiState.value = UiState.Success(data) },
                    onFailure = { error -> _uiState.value = UiState.Error(error.message ?: "Error desconocido") }
                )
            }
        }
    }
}