package com.example.capucareapp

import com.example.capucareapp.ui.viewmodel.MainViewModel
import com.example.capucareapp.ui.viewmodel.UiState

import com.example.capucareapp.data.repository.AppRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repositoryMock = mockk<AppRepository>()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(repositoryMock)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cuando se piden datos al backend, el estado cambia a Success`() = runTest {
        // 1. Preparar datos falsos
        val mockData = listOf(Any(), Any())
        coEvery { repositoryMock.getBackendData() } returns flowOf(Result.success(mockData))

        // 2. Ejecutar
        viewModel.fetchBackendData()
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Verificar
        val currentState = viewModel.uiState.value
        assertEquals(UiState.Success(mockData), currentState)
    }
}