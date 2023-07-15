package com.dangjang.android.presentation

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.IntroVO
import com.dangjang.android.domain.usecase.GetIntroUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val getIntroUseCase: GetIntroUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _introDataFlow = MutableStateFlow(IntroVO())
    val introDataFlow = _introDataFlow.asStateFlow()

    fun getIntroData() {
        viewModelScope.launch {
            getIntroUseCase.getIntro().collect{
                if (it is Exception)
                    Toast.makeText(getApplication<Application>().applicationContext,it.message,Toast.LENGTH_SHORT).show()
                if (it is IntroVO)
                    _introDataFlow.emit(it)
            }
        }
    }

}