package com.dangjang.android.presentation.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
) : ViewModel() {

    private val _exerciseList = MutableStateFlow(ArrayList<Int>())
    val exerciseList = _exerciseList.asStateFlow()

}