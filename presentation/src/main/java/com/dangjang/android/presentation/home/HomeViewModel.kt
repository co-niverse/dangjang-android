package com.dangjang.android.presentation.home

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.HealthMetricVO
import com.dangjang.android.domain.request.AddHealthMetricRequest
import com.dangjang.android.domain.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeUseCase: HomeUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _addHealthMetricFlow = MutableStateFlow(HealthMetricVO())
    val addHealthMetricFlow = _addHealthMetricFlow.asStateFlow()

    private val _addHealthMetricRequest = MutableStateFlow(AddHealthMetricRequest())
    val addHealthMetricRequest = _addHealthMetricRequest.asStateFlow()

    fun addHealthMetric(
        addHealthMetricRequest: AddHealthMetricRequest
    ) {
        viewModelScope.launch {
            getHomeUseCase.addHealthMetric(addHealthMetricRequest)
                .onEach {
                    _addHealthMetricFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

    private fun <T> Flow<T>.handleErrors(): Flow<T> =
        catch { e -> Toast.makeText(getApplication<Application>().applicationContext,e.message,
            Toast.LENGTH_SHORT).show() }

    fun setType(type: String) {
        _addHealthMetricRequest.update {
            it.copy(type = type)
        }
    }

    fun setCreatedAt(createdAt: String) {
        _addHealthMetricRequest.update {
            it.copy(createdAt = createdAt)
        }
    }

    fun setUnit(unit: String) {
        _addHealthMetricRequest.update {
            it.copy(unit = unit)
        }
    }
}