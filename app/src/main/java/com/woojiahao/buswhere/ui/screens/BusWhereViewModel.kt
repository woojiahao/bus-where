package com.woojiahao.buswhere.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woojiahao.buswhere.network.BusWhereApi
import com.woojiahao.buswhere.network.BusWhereService
import kotlinx.coroutines.launch
import okio.IOException

sealed interface BusWhereUiState {
    data class Success(val services: List<BusWhereService>) : BusWhereUiState
    object Error : BusWhereUiState
    object Loading : BusWhereUiState
}

class BusWhereViewModel : ViewModel() {
    var busWhereUiState : BusWhereUiState by mutableStateOf(BusWhereUiState.Loading)
        private set

    init {
//        getBusWhereRoutes()
        getBusWhereServices()
    }

    fun getBusWhereRoutes() {
//        viewModelScope.launch {
//            val listResults = BusWhereApi.retrofitService.getRoutes()
//            busWhereUiState = listResults
//        }
    }

    fun getBusWhereServices() {
        viewModelScope.launch {
            busWhereUiState = try {
                val listResults = BusWhereApi.retrofitService.getServices()
                BusWhereUiState.Success(listResults)
            } catch (e: IOException) {
                BusWhereUiState.Error
            }
        }
    }
}