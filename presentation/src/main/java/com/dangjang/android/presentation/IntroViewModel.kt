package com.dangjang.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.IntroVO
import com.dangjang.android.domain.usecase.GetIntroUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val getIntroUseCase: GetIntroUseCase
) : ViewModel() {

    private val _introDataFlow = MutableStateFlow(IntroVO())
    val introDataFlow = _introDataFlow.asStateFlow()

    fun getIntroData() {
        viewModelScope.launch {
            getIntroUseCase.getIntro().collect{
                _introDataFlow.emit(it)
            }
        }
    }

}