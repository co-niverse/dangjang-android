package com.dangjang.android.presentation.mypage

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.AUTO_LOGIN_EDITOR_KEY
import com.dangjang.android.domain.constants.AUTO_LOGIN_SPF_KEY
import com.dangjang.android.domain.constants.FCM_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.logging.MypageScreenExposureScheme
import com.dangjang.android.domain.logging.PointScreenExposureScheme
import com.dangjang.android.domain.model.GetMypageVO
import com.dangjang.android.domain.model.GetPointVO
import com.dangjang.android.domain.model.PostPointVO
import com.dangjang.android.domain.request.PostPointRequest
import com.dangjang.android.domain.usecase.MypageUseCase
import com.dangjang.android.domain.usecase.TokenUseCase
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
class MypageViewModel @Inject constructor(
    private val getTokenUseCase: TokenUseCase,
    private val getMypageUseCase: MypageUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _getMypageFlow = MutableStateFlow(GetMypageVO())
    val getMypageFlow = _getMypageFlow.asStateFlow()

    private val _getPointFlow = MutableStateFlow(GetPointVO())
    val getPointFlow = _getPointFlow.asStateFlow()

    private val _postPointRequest = MutableStateFlow(PostPointRequest())
    val postPointRequest = _postPointRequest.asStateFlow()

    private val _postPointFlow = MutableStateFlow(PostPointVO())
    val postPointFlow = _postPointFlow.asStateFlow()

    private val _selectedGiftTitle = MutableStateFlow(String())
    val selectedGiftTitle = _selectedGiftTitle.asStateFlow()

    private val _selectedGiftPrice = MutableStateFlow(String())
    val selectedGiftPrice = _selectedGiftPrice.asStateFlow()

    private val _selectedGiftPhone = MutableStateFlow(String())
    val selectedGiftPhone = _selectedGiftPhone.asStateFlow()

    private val _selectedGiftName = MutableStateFlow(String())
    val selectedGiftName = _selectedGiftName.asStateFlow()

    private val _selectedGiftComment = MutableStateFlow(String())
    val selectedGiftComment = _selectedGiftComment.asStateFlow()

    private val _signoutFlow = MutableStateFlow(false)
    val signoutFlow = _signoutFlow.asStateFlow()

    private val _logoutFlow = MutableStateFlow(false)
    val logoutFlow = _logoutFlow.asStateFlow()

    private val _reissueTokenFlow = MutableStateFlow(false)
    val reissueTokenFlow = _reissueTokenFlow.asStateFlow()

    private val _goToLoginActivityFlow = MutableStateFlow(false)
    val goToLoginActivityFlow = _goToLoginActivityFlow.asStateFlow()

    fun getMypage(accessToken: String) {
        viewModelScope.launch {
            getMypageUseCase.getMypage("Bearer $accessToken")
                .onEach {
                    _getMypageFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

    fun getPoint(accessToken: String) {
        viewModelScope.launch {
            getMypageUseCase.getPoint("Bearer $accessToken")
                .onEach {
                    _getPointFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

    fun setPostPointRequest(type: String, phone: String, name: String, comment: String) {
        _postPointRequest.update {
            it.copy(type = type, phone = phone, name = name, comment = comment)
        }
    }

    fun setSelectedGiftTitle(title: String) {
        _selectedGiftTitle.update {
            title
        }
    }

    fun setSelectedGiftPrice(price: String) {
        _selectedGiftPrice.update {
            price
        }
    }

    fun setSelectedGiftPhone(phone: String) {
        _selectedGiftPhone.update {
            phone
        }
    }

    fun setSelectedGiftName(name: String) {
        _selectedGiftName.update {
            name
        }
    }

    fun setSelectedGiftComment(comment: String) {
        _selectedGiftComment.update {
            comment
        }
    }

    fun postPoint(accessToken: String) {
        viewModelScope.launch {
            getMypageUseCase.postPoint("Bearer $accessToken", postPointRequest.value)
                .onEach {
                    _postPointFlow.emit(it)
                }
                .handlePointErrors()
                .collect()
        }
    }

    fun logout(accessToken: String, fcmToken: String) {
        viewModelScope.launch {
            getMypageUseCase.logout("Bearer $accessToken", fcmToken)
                .onEach {
                    _logoutFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

    fun signout(accessToken: String) {
        viewModelScope.launch {
            getMypageUseCase.signout("Bearer $accessToken")
                .onEach {
                    _signoutFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = getApplication<Application>().applicationContext.getSharedPreferences(
            TOKEN_SPF_KEY, Context.MODE_PRIVATE)

        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    fun removeAutoLoginProviderSpf() {
        val sp: SharedPreferences = getApplication<Application>().applicationContext.getSharedPreferences(
            AUTO_LOGIN_SPF_KEY,
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = sp.edit()
        editor.remove(AUTO_LOGIN_EDITOR_KEY)
        editor.apply()
    }

    fun removeAccessTokenSpf() {
        val sp: SharedPreferences = getApplication<Application>().applicationContext.getSharedPreferences(
            TOKEN_SPF_KEY,
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = sp.edit()
        editor.remove(ACCESS_TOKEN_KEY)
        editor.apply()
    }

    fun removeFcmTokenSpf() {
        val sp: SharedPreferences = getApplication<Application>().applicationContext.getSharedPreferences(
            FCM_TOKEN_KEY,
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = sp.edit()
        editor.remove(FCM_TOKEN_KEY)
        editor.apply()
    }


    //Logging
    fun shotMypageExposureLogging(stayTime: Double) {
        val scheme = getMypageExposureLoggingScheme(stayTime)
        SWMLogging.logEvent(scheme)
    }

    private fun getMypageExposureLoggingScheme(stayTime: Double): ExposureScheme {
        return MypageScreenExposureScheme.Builder()
            .setStayTime(stayTime)
            .build()
    }

    fun shotPointExposureLogging(stayTime: Double) {
        val scheme = getPointExposureLoggingScheme(stayTime)
        SWMLogging.logEvent(scheme)
    }

    private fun getPointExposureLoggingScheme(stayTime: Double): ExposureScheme {
        return PointScreenExposureScheme.Builder()
            .setStayTime(stayTime)
            .build()
    }

    private fun <T> Flow<T>.handlePointErrors(): Flow<T> =
        catch { e ->
            Toast.makeText(
                getApplication<Application>().applicationContext, "포인트 구매에 실패했습니다. 다시 한번 시도해주세요.",
                Toast.LENGTH_SHORT
            ).show()
            if (e.message.toString() == "401 : 만료된 토큰입니다.") {
                getTokenUseCase.reissueToken(getAccessToken() ?: "")
                    .onEach {
                        _reissueTokenFlow.emit(it)
                    }
                    .handleReissueTokenErrors()
                    .collect()
                Toast.makeText(
                    getApplication<Application>().applicationContext, "로그인이 만료되었습니다. 다시 한번 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
//            Toast.makeText(
//                getApplication<Application>().applicationContext, e.message,
//                Toast.LENGTH_SHORT
//            ).show()
        }

    private fun <T> Flow<T>.handleErrors(): Flow<T> =
        catch { e ->
            Log.e("error",e.message.toString())
            if (e.message == null) {
                _goToLoginActivityFlow.value = true
                Toast.makeText(
                    getApplication<Application>().applicationContext, "회원탈퇴가 완료되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (e.message.toString() == "401 : 만료된 토큰입니다.") {
                getTokenUseCase.reissueToken(getAccessToken() ?: "")
                    .onEach {
                        _reissueTokenFlow.emit(it)
                    }
                    .handleReissueTokenErrors()
                    .collect()
                Toast.makeText(
                    getApplication<Application>().applicationContext, "로그인이 만료되었습니다. 다시 한번 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
//            Toast.makeText(
//                getApplication<Application>().applicationContext, e.message,
//                Toast.LENGTH_SHORT
//            ).show()
        }

    private fun <T> Flow<T>.handleReissueTokenErrors(): Flow<T> =
        catch { e ->
            Log.e("error",e.message.toString())
            // refreshToken까지 만료된 경우 -> 로그인 화면으로 이동
            if (e.message.toString() == "401 : 만료된 토큰입니다.") {
                _goToLoginActivityFlow.value = true
                Toast.makeText(
                    getApplication<Application>().applicationContext, "로그인이 필요합니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
//            Toast.makeText(
//                getApplication<Application>().applicationContext, e.message,
//                Toast.LENGTH_SHORT
//            ).show()
        }
}