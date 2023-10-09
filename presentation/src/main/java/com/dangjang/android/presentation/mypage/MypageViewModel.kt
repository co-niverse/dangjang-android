package com.dangjang.android.presentation.mypage

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.GetMypageVO
import com.dangjang.android.domain.model.GetPointVO
import com.dangjang.android.domain.model.PostPointVO
import com.dangjang.android.domain.request.PostPointRequest
import com.dangjang.android.domain.usecase.MypageUseCase
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

    private val _signoutFlow = MutableStateFlow(false)
    val signoutFlow = _signoutFlow.asStateFlow()

    private val _logoutFlow = MutableStateFlow(false)
    val logoutFlow = _logoutFlow.asStateFlow()

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

    fun setPostPointRequest(type: String, phone: String) {
        _postPointRequest.update {
            it.copy(type = type, phone = phone)
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

    fun postPoint(accessToken: String) {
        viewModelScope.launch {
            getMypageUseCase.postPoint("Bearer $accessToken", postPointRequest.value)
                .onEach {
                    _postPointFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

    fun logout(accessToken: String, fcmToken: String) {
        viewModelScope.launch {
            getMypageUseCase.logout("Bearer $accessToken", fcmToken)
                .onEach {
                    _signoutFlow.emit(it)
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

    private fun <T> Flow<T>.handleErrors(): Flow<T> =
        catch { e ->
            Log.e("error",e.message.toString())
            Toast.makeText(
                getApplication<Application>().applicationContext, e.message,
                Toast.LENGTH_SHORT
            ).show()
        }
}