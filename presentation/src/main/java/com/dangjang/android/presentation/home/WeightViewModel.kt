package com.dangjang.android.presentation.home

import androidx.lifecycle.ViewModel
import com.dangjang.android.domain.model.BloodPressureVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject constructor(
) : ViewModel() {

    private val _weightList = MutableStateFlow(ArrayList<BloodPressureVO>())
    val weightList = _weightList.asStateFlow()

}