package com.dangjang.android.presentation.signup

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.HttpResponseStatus
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.FCM_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.logging.SignupActiveScheme
import com.dangjang.android.domain.logging.SignupAgreeScheme
import com.dangjang.android.domain.logging.SignupBodyScheme
import com.dangjang.android.domain.logging.SignupDiagnosisScheme
import com.dangjang.android.domain.logging.SignupDiseaseScheme
import com.dangjang.android.domain.logging.SignupGenderBirthScheme
import com.dangjang.android.domain.logging.SignupMediScheme
import com.dangjang.android.domain.logging.SignupNicknameScheme
import com.dangjang.android.domain.model.DuplicateNicknameVO
import com.dangjang.android.domain.model.AuthVO
import com.dangjang.android.domain.request.PostFcmTokenRequest
import com.dangjang.android.domain.requestVO.SignupRequestVO
import com.dangjang.android.domain.usecase.SignupUseCase
import com.dangjang.android.domain.usecase.TokenUseCase
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.login.LoginActivity
import com.dangjang.android.swm_logging.SWMLogging
import com.dangjang.android.swm_logging.logging_scheme.ExposureScheme
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
class SignupViewModel @Inject constructor(
    private val getSignupUseCase: SignupUseCase,
    private val getTokenUseCase: TokenUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _duplicateNicknameFlow = MutableStateFlow(DuplicateNicknameVO())
    val duplicateNicknameFlow = _duplicateNicknameFlow.asStateFlow()

    private val _signupFlow = MutableStateFlow(AuthVO())
    val signupFlow = _signupFlow.asStateFlow()

    private val _signupRequest = MutableStateFlow(SignupRequestVO())
    val signupRequest = _signupRequest.asStateFlow()

    private val _startMainActivity = MutableStateFlow(HttpResponseStatus.NONE)
    val startMainActivity = _startMainActivity.asStateFlow()

    private val _goToLoginActivity = MutableStateFlow(false)
    val goToLoginActivity = _goToLoginActivity.asStateFlow()

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
//                    _startMainActivity.emit(HttpResponseStatus.OK)
                    if (it.nickname != "") {
                        postFcmToken()
                    }
                }
                .handleErrors()
                .collect()
        }
    }

    private fun postFcmToken(
    ) {
        viewModelScope.launch {
            getTokenUseCase.postFcmToken(getAccessToken() ?: "", PostFcmTokenRequest(getFCMToken() ?: ""))
                .onEach {
                    if (it) {
                        _startMainActivity.emit(HttpResponseStatus.OK)
                    }
                }
                .handleErrors()
                .collect()
        }
    }

    fun setProvider(provider: String) {
        _signupRequest.update {
            it.copy(provider = provider)
        }
    }

    fun setAccessToken(accessToken: String) {
        _signupRequest.update {
            it.copy(accessToken = accessToken)
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
        catch { e ->
            Log.e("error",e.message.toString())
            if (e.message.toString() == "409 : 이미 존재하는 데이터입니다.") {
                Log.e("test","로그인 화면으로 이동")
                _goToLoginActivity.value = true
            }
        }


    private val _diagnosisFlag = MutableStateFlow(false)
    val diagnosisFlag = _diagnosisFlag.asStateFlow()

    fun setDiagnosisFlag(flag: Boolean) {
        _diagnosisFlag.value = flag
    }

    fun getDiagnosisYearList(): ArrayList<String> {
        val yearList = arrayListOf<String>()

        for (i in 1..20) {
            yearList.add(i.toString())
        }
        yearList.add("20년 이상")

        return yearList
    }

    fun setGreenTextColor(): Int {
        return Color.parseColor("#32CC42")
    }

    fun setGreenBackgroundResource(): Int {
        return R.drawable.background_round_green
    }

    fun setGrayTextColor(): Int {
        return Color.parseColor("#878787")
    }

    fun setGrayBackgroundResource(): Int {
        return R.drawable.background_round_gray
    }

    fun setGreenBtnBackgroundResource(): Int {
        return R.drawable.background_green_gradient
    }

    //Logging
    fun shotSignupNicknameLogging(stayTime: Double) {
        val scheme = getSignupNicknameLoggingScheme(stayTime)
        SWMLogging.logEvent(scheme)
    }

    private fun getSignupNicknameLoggingScheme(stayTime: Double): ExposureScheme {
        return SignupNicknameScheme.Builder()
            .setStayTime(stayTime)
            .build()
    }

    fun shotSignupGenderBirthLogging(stayTime: Double) {
        val scheme = getSignupGenderBirthLoggingScheme(stayTime)
        SWMLogging.logEvent(scheme)
    }

    private fun getSignupGenderBirthLoggingScheme(stayTime: Double): ExposureScheme {
        return SignupGenderBirthScheme.Builder()
            .setStayTime(stayTime)
            .build()
    }

    fun shotSignupBodyLogging(stayTime: Double) {
        val scheme = getSignupBodyLoggingScheme(stayTime)
        SWMLogging.logEvent(scheme)
    }

    private fun getSignupBodyLoggingScheme(stayTime: Double): ExposureScheme {
        return SignupBodyScheme.Builder()
            .setStayTime(stayTime)
            .build()
    }

    fun shotSignupActiveLogging(stayTime: Double) {
        val scheme = getSignupActiveLoggingScheme(stayTime)
        SWMLogging.logEvent(scheme)
    }

    private fun getSignupActiveLoggingScheme(stayTime: Double): ExposureScheme {
        return SignupActiveScheme.Builder()
            .setStayTime(stayTime)
            .build()
    }

    fun shotSignupDiagnosisLogging(stayTime: Double) {
        val scheme = getSignupDiagnosisLoggingScheme(stayTime)
        SWMLogging.logEvent(scheme)
    }

    private fun getSignupDiagnosisLoggingScheme(stayTime: Double): ExposureScheme {
        return SignupDiagnosisScheme.Builder()
            .setStayTime(stayTime)
            .build()
    }

    fun shotSignupMediLogging(stayTime: Double) {
        val scheme = getSignupMediLoggingScheme(stayTime)
        SWMLogging.logEvent(scheme)
    }

    private fun getSignupMediLoggingScheme(stayTime: Double): ExposureScheme {
        return SignupMediScheme.Builder()
            .setStayTime(stayTime)
            .build()
    }

    fun shotSignupDiseaseLogging(stayTime: Double) {
        val scheme = getSignupDiseaseLoggingScheme(stayTime)
        SWMLogging.logEvent(scheme)
    }

    private fun getSignupDiseaseLoggingScheme(stayTime: Double): ExposureScheme {
        return SignupDiseaseScheme.Builder()
            .setStayTime(stayTime)
            .build()
    }

    fun shotSignupAgreeLogging(stayTime: Double) {
        val scheme = getSignupAgreeLoggingScheme(stayTime)
        SWMLogging.logEvent(scheme)
    }

    private fun getSignupAgreeLoggingScheme(stayTime: Double): ExposureScheme {
        return SignupAgreeScheme.Builder()
            .setStayTime(stayTime)
            .build()
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = getApplication<Application>().applicationContext.getSharedPreferences(
            TOKEN_SPF_KEY, Context.MODE_PRIVATE)

        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    private fun getFCMToken(): String? {
        val sharedPreferences = getApplication<Application>().applicationContext.getSharedPreferences(
            FCM_TOKEN_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(FCM_TOKEN_KEY, null)
    }

}