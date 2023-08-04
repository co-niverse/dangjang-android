package com.dangjang.android.presentation

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.DuplicateNicknameVO
import com.dangjang.android.domain.usecase.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val getSignupUseCase: SignupUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _duplicateNicknameFlow = MutableStateFlow(DuplicateNicknameVO())
    val duplicateNicknameFlow = _duplicateNicknameFlow.asStateFlow()

    fun getDuplicateNickname(nickname: String) {
        viewModelScope.launch {
            getSignupUseCase.getDuplicateNickname(nickname)
                .onEach {
                    _duplicateNicknameFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

    private fun <T> Flow<T>.handleErrors(): Flow<T> =
        catch { e -> Toast.makeText(getApplication<Application>().applicationContext,e.message,
            Toast.LENGTH_SHORT).show() }

}