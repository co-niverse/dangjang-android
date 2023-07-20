package com.dangjang.android.presentation

import android.app.Application
import android.os.Build
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.Mass
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.HealthConnectVO
import com.dangjang.android.domain.model.IntroVO
import com.dangjang.android.domain.usecase.GetIntroUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val getIntroUseCase: GetIntroUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _introDataFlow = MutableStateFlow(IntroVO())
    val introDataFlow = _introDataFlow.asStateFlow()

    private val _healthConnectFlow = MutableStateFlow(HealthConnectVO())
    val healthConnectFlow = _healthConnectFlow.asStateFlow()

    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(getApplication<Application>().applicationContext) }

    fun checkAvailability() {
        Log.e("vm-sdkStatus", HealthConnectClient.sdkStatus(getApplication<Application>().applicationContext).toString())
        //설치여부 확인
        var sdkStatus = HealthConnectClient.sdkStatus(getApplication<Application>().applicationContext)
        if (sdkStatus == HealthConnectClient.SDK_AVAILABLE) { // 설치 되어 있음
            //healthConnectVO를 1로 업데이트
            _healthConnectFlow.update { HealthConnectVO(1) }
        }
        if (sdkStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
            Log.e("HealthConnect-ERROR","헬스커넥트 업데이트가 필요합니다.")
        }
        if (sdkStatus == HealthConnectClient.SDK_UNAVAILABLE) {
            Log.e("HealthConnect-ERROR","헬스커넥트를 설치할 수 없습니다.")
        }
    }

    val permissions = setOf(
        //체중
        HealthPermission.getReadPermission(WeightRecord::class)
    )

    var permissionsGranted = false

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHealthConnect() {
        viewModelScope.launch {
            tryWithPermissionsCheck()
        }
    }

    // 권한 체크를 적용하고 에러를 핸들링한다.
    private suspend fun tryWithPermissionsCheck() {
        Log.e("94","94")
        permissionsGranted = hasAllPermissions(permissions)
        if (permissionsGranted) {
            // TODO : 데이터 읽기
        } else {
            Log.e("GRANT-ERROR","권한이 허용되지 않았습니다.")
        }
    }

    suspend fun hasAllPermissions(permissions: Set<String>): Boolean { //허가 받기
        return healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)
    }



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