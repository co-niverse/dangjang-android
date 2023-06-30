package com.dangjang.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.LoginVO
import com.dangjang.android.domain.usecase.GetLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getLoginUseCase: GetLoginUseCase
) : ViewModel() {

    private val _loginDataFlow = MutableStateFlow(LoginVO())
    val loginDataFlow = _loginDataFlow.asStateFlow()

    fun fetchLoginData(accessToken: String) {
        viewModelScope.launch {
            getLoginUseCase(accessToken).collectLatest {
                _loginDataFlow.emit(it)
            }
        }
    }

}