package com.dangjang.android.presentation

import android.app.Application
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
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
import java.time.Instant
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
        HealthPermission.getReadPermission(WeightRecord::class),
        HealthPermission.getReadPermission(BloodGlucoseRecord::class),
        HealthPermission.getReadPermission(BloodPressureRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class)
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
        permissionsGranted = hasAllPermissions(permissions)
        if (permissionsGranted) {
            readWeight()
            readBloodGlucose()
            readBloodPressureRecord()
            readSleepSession()
            readSteps()
        } else {
            Log.e("GRANT-ERROR","권한이 허용되지 않았습니다.")
        }
    }

    suspend fun hasAllPermissions(permissions: Set<String>): Boolean { //허가 받기
        return healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)
    }

    //체중
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun readWeight() {
        val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val endOfWeek = startOfDay.toInstant().plus(7,ChronoUnit.DAYS)
        // 헬스 커넥트 체중 읽기
        readWeightInput(startOfDay.toInstant(),endOfWeek)
        Log.e("HC-Weight",readWeightInput(startOfDay.toInstant(),endOfWeek).toString())
    }

    suspend fun readWeightInput(start: Instant, end: Instant): List<WeightRecord> {
        val request = ReadRecordsRequest(
            recordType = WeightRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }

    //혈당
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun readBloodGlucose() {
        val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val endOfWeek = startOfDay.toInstant().plus(7,ChronoUnit.DAYS)
        // 헬스 커넥트 체중 읽기
        readBloodGlucoseInput(startOfDay.toInstant(),endOfWeek)
        Log.e("HC-BloodGlucose",readBloodGlucoseInput(startOfDay.toInstant(),endOfWeek).toString())
    }

    private suspend fun readBloodGlucoseInput(start: Instant, end: Instant): List<BloodGlucoseRecord> {
        val request = ReadRecordsRequest(
            recordType = BloodGlucoseRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }

    //혈압
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun readBloodPressureRecord() {
        val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val endOfWeek = startOfDay.toInstant().plus(7,ChronoUnit.DAYS)
        // 헬스 커넥트 체중 읽기
        readBloodPressureInput(startOfDay.toInstant(),endOfWeek)
        Log.e("HC-BloodPressure",readBloodPressureInput(startOfDay.toInstant(),endOfWeek).toString())
    }

    private suspend fun readBloodPressureInput(start: Instant, end: Instant): List<BloodPressureRecord> {
        val request = ReadRecordsRequest(
            recordType = BloodPressureRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }

    //수면
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun readSleepSession() {
        //TODO : 시간 범위 수정
        val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val endOfWeek = startOfDay.toInstant().plus(7,ChronoUnit.DAYS)
        readSleepSessionInput(startOfDay.toInstant(),endOfWeek)
        Log.e("HC-Sleep",readSleepSessionInput(startOfDay.toInstant(),endOfWeek).toString())
        readSleepDuration(startOfDay.toInstant(),endOfWeek)
        Log.e("HC-SleepDuration",readSleepDuration(startOfDay.toInstant(),endOfWeek).toString())
    }

    private suspend fun readSleepSessionInput(start: Instant, end: Instant): List<SleepSessionRecord> {
        val request = ReadRecordsRequest(
            recordType = SleepSessionRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }

    //수면 시간
    private suspend fun readSleepDuration(start: Instant, end: Instant): java.time.Duration? {
        val request = AggregateRequest(
            metrics = setOf(SleepSessionRecord.SLEEP_DURATION_TOTAL),
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.aggregate(request)
        return response[SleepSessionRecord.SLEEP_DURATION_TOTAL]
    }

    //걸음수
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun readSteps() {
        val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val endOfWeek = startOfDay.toInstant().plus(7,ChronoUnit.DAYS)
        readStepsInput(startOfDay.toInstant(),endOfWeek)
        Log.e("HC-Steps",readStepsInput(startOfDay.toInstant(),endOfWeek).toString())
    }

    private suspend fun readStepsInput(start: Instant, end: Instant): List<StepsRecord> {
        val request = ReadRecordsRequest(
            recordType = StepsRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
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