package com.dangjang.android.presentation.home

import androidx.lifecycle.ViewModel
import com.dangjang.android.domain.model.GlucoseListVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GlucoseViewModel @Inject constructor(
) : ViewModel() {

    private val _glucoseList = MutableStateFlow(ArrayList<GlucoseListVO>())
    val glucoseList = _glucoseList.asStateFlow()

}