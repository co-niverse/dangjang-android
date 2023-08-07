package com.dangjang.android.presentation

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.DuplicateNicknameVO
import com.dangjang.android.domain.model.SignupVO
import com.dangjang.android.domain.requestVO.SignupRequestVO
import com.dangjang.android.domain.usecase.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val getSignupUseCase: SignupUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _duplicateNicknameFlow = MutableStateFlow(DuplicateNicknameVO())
    val duplicateNicknameFlow = _duplicateNicknameFlow.asStateFlow()

    private val _signupFlow = MutableStateFlow(SignupVO())
    val signupFlow = _signupFlow.asStateFlow()

    private val _signupRequest = MutableStateFlow(SignupRequestVO())
    val signupRequest = _signupRequest.asStateFlow()

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

    fun signup(
        data: SignupRequestVO
    ) {
        viewModelScope.launch {
            getSignupUseCase.signup(data)
                .onEach {
                    _signupFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

    fun setNickname(nickname: String) {
        _signupRequest.update {
            it.copy(nickname = nickname)
        }
    }

    fun setGender(gender: Boolean) {
        _signupRequest.update {
            it.copy(gender = gender)
        }
    }

    fun setBirthday(birthday: String) {
        _signupRequest.update {
            it.copy(birthday = birthday)
        }
    }

    fun setBody(height: Int, weight: Int) {
        _signupRequest.update {
            it.copy(height = height, weight = weight)
        }
    }

    fun setActivityAmount(activeAmount: String) {
        _signupRequest.update {
            it.copy(activityAmount = activeAmount)
        }
    }

    fun setDiabetes(diabetes: Boolean, diabetesYear: Int) {
        _signupRequest.update {
            it.copy(diabetes = diabetes, diabetes_year = diabetesYear)
        }
    }

    fun setMedi(medicine: Boolean, injection: Boolean) {
        _signupRequest.update {
            it.copy(medicine = medicine, injection = injection)
        }
    }

    fun setDiseases(diseases: List<String>) {
        _signupRequest.update {
            it.copy(diseases = diseases)
        }
    }

    private fun <T> Flow<T>.handleErrors(): Flow<T> =
        catch { e -> Toast.makeText(getApplication<Application>().applicationContext,e.message,
            Toast.LENGTH_SHORT).show() }

}