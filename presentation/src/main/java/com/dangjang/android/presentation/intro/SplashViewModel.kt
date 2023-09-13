package com.dangjang.android.presentation.intro

import android.app.Application
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodGlucoseRecord.Companion.RELATION_TO_MEAL_INT_TO_STRING_MAP
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.MealType.MEAL_TYPE_INT_TO_STRING_MAP
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.HealthConnectVO
import com.dangjang.android.domain.model.IntroVO
import com.dangjang.android.domain.usecase.SplashUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val splashUseCase: SplashUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _introDataFlow = MutableStateFlow(IntroVO())
    val introDataFlow = _introDataFlow.asStateFlow()

    private val _healthConnectFlow = MutableStateFlow(HealthConnectVO())
    val healthConnectFlow = _healthConnectFlow.asStateFlow()

    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(getApplication<Application>().applicationContext) }

    lateinit var weightList: List<WeightRecord>
    lateinit var stepList: List<StepsRecord>
    lateinit var exerciseList: List<ExerciseSessionRecord>
    lateinit var bloodGlucoseList: List<BloodGlucoseRecord>

    private val weightPermission = setOf(
        HealthPermission.getReadPermission(WeightRecord::class)
    )
    private val bloodGlucosePermission = setOf(
        HealthPermission.getReadPermission(BloodGlucoseRecord::class)
    )
    private val stepsPermission = setOf(
        HealthPermission.getReadPermission(StepsRecord::class)
    )
    private val exerciseSessionPermission = setOf(
        HealthPermission.getReadPermission(ExerciseSessionRecord::class)
    )

    var weightPermissionGranted = false
    var bloodGlucosePermissionGranted = false
    var stepsPermissionGranted = false
    var exerciseSessionPermissionGranted = false

    fun checkAvailability() {
        //설치여부 확인
        var sdkStatus = HealthConnectClient.sdkStatus(getApplication<Application>().applicationContext)
        _healthConnectFlow.update { HealthConnectVO(sdkStatus) }
        when (sdkStatus) {
            HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED -> Log.e("HealthConnect-ERROR","헬스커넥트 설치나 업데이트가 필요합니다.")
            HealthConnectClient.SDK_UNAVAILABLE -> Log.e("HealthConnect-ERROR","헬스커넥트를 설치할 수 없습니다.")
            HealthConnectClient.SDK_AVAILABLE -> Log.e("HealthConnect-status","헬스커넥트가 설치되어 있습니다.")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHealthConnect() {
        viewModelScope.launch {
            tryWithWeightPermissionsCheck()
            tryWithBloodGlucosePermissionsCheck()
            tryWithStepsPermissionCheck()
            tryWithExerciseSessionPermissionCheck()
        }
    }
    private suspend fun tryWithWeightPermissionsCheck() {
        weightPermissionGranted = hasAllPermissions(weightPermission)
        if (weightPermissionGranted) {
            readWeight()
        } else {
            Log.e("GRANT-ERROR","체중 권한이 허용되지 않았습니다.")
        }
    }

    private suspend fun tryWithBloodGlucosePermissionsCheck() {
        bloodGlucosePermissionGranted = hasAllPermissions(bloodGlucosePermission)
        if (bloodGlucosePermissionGranted) {
            readBloodGlucose()
        } else {
            Log.e("GRANT-ERROR","혈당 권한이 허용되지 않았습니다.")
        }
    }

    private suspend fun tryWithStepsPermissionCheck() {
        stepsPermissionGranted = hasAllPermissions(stepsPermission)
        if (stepsPermissionGranted) {
            readSteps()
        } else {
            Log.e("GRANT-ERROR","걸음수 권한이 허용되지 않았습니다.")
        }
    }

    private suspend fun tryWithExerciseSessionPermissionCheck() {
        exerciseSessionPermissionGranted = hasAllPermissions(exerciseSessionPermission)
        if (exerciseSessionPermissionGranted) {
            readExerciseSession()
        } else {
            Log.e("GRANT-ERROR","운동 권한이 허용되지 않았습니다.")
        }
    }

    suspend fun hasAllPermissions(permissions: Set<String>): Boolean { //허가 받기
        return healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)
    }

    //체중
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun readWeight() {
        //TODO : 로그인 시작 시간 처리 후 getTodayStartTime() -> getStartTime() 함수로 대치
        weightList = readWeightRecord(getTodayStartTime(), getNowTime())
        for (weightRecord in weightList) {
            Log.e("HC-Weight",weightRecord.weight.toString())
        }
    }

    private suspend fun readWeightRecord(start: Instant, end: Instant): List<WeightRecord> {
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
        bloodGlucoseList = readBloodGlucoseRecord(getTodayStartTime(), getNowTime())
        for (bloodGlucoseRecord in bloodGlucoseList) {
            val bgTime = changeInstantToKST(bloodGlucoseRecord.time)
            val mealType = MEAL_TYPE_INT_TO_STRING_MAP.get(bloodGlucoseRecord.mealType)
            val relationToMeal = RELATION_TO_MEAL_INT_TO_STRING_MAP.get(bloodGlucoseRecord.relationToMeal)
            Log.e("HC-BloodGlucose",bgTime + "시: " + "("+ mealType + ", "+relationToMeal + ") " + bloodGlucoseRecord.level.inMilligramsPerDeciliter.roundToInt() + "mg/dL" )
        }
    }

    private suspend fun readBloodGlucoseRecord(start: Instant, end: Instant): List<BloodGlucoseRecord> {
        val request = ReadRecordsRequest(
            recordType = BloodGlucoseRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }

    //걸음수
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun readSteps() {
        stepList = readStepsRecord(getTodayStartTime(), getNowTime())
        for (stepsRecord in stepList) {
            Log.e("HC-Steps",stepsRecord.count.toString()+"보")
        }
    }

    private suspend fun readStepsRecord(start: Instant, end: Instant): List<StepsRecord> {
        val request = ReadRecordsRequest(
            recordType = StepsRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }

    //운동
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun readExerciseSession() {
        exerciseList = readExerciseSessionRecord(getTodayStartTime(),getNowTime())
        for (exerciseRecord in exerciseList) {
            val exerciseName = ExerciseSessionRecord.EXERCISE_TYPE_INT_TO_STRING_MAP.get(exerciseRecord.exerciseType)
            val exerciseStartTime = changeInstantToKST(exerciseRecord.startTime)
            val exerciseEndTime = changeInstantToKST(exerciseRecord.endTime)
            Log.e("HC-Exercise", "운동 종류: $exerciseName 시간: $exerciseStartTime to $exerciseEndTime")
        }
    }

    private suspend fun readExerciseSessionRecord(start: Instant, end: Instant): List<ExerciseSessionRecord> {
        val request = ReadRecordsRequest(
            recordType = ExerciseSessionRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }

    private fun changeInstantToKST(instant: Instant): String {
        val koreaZoneId = ZoneId.of("Asia/Seoul")
        val koreaTime = instant.atZone(koreaZoneId)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = koreaTime.format(formatter)
        return formattedDateTime
    }

    fun getIntroData() {
        viewModelScope.launch {
            splashUseCase.getIntro()
                .onEach {
                    _introDataFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

    private fun <T> Flow<T>.handleErrors(): Flow<T> =
        catch { e -> Toast.makeText(getApplication<Application>().applicationContext,e.message,Toast.LENGTH_SHORT).show() }

    private fun getTodayStartTime(): Instant {
        return ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).toInstant()
    }

    private fun getNowTime(): Instant {
        return ZonedDateTime.now().toInstant()
    }

//    private fun getStartTime(): Instant? {
//        // TODO : Intro API에서 내려주는 최근 로그인 시간으로 처리 (Ex. introDataFlow.value.time)
//        // null일 때는 첫 연동이니까 오늘 시작 시간으로 처리
//        if (introDataFlow.value.time == null) {
//            return getTodayStartTime()
//        } else {
//            return return convertDateTimeStirngToInstant("")
//        }
//    }

    private fun convertDateTimeStirngToInstant(dateTimeString: String): Instant? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

        return try {
            val parsedInstant = Instant.from(formatter.parse(dateTimeString))
            parsedInstant
        } catch (e: DateTimeParseException) {
            e.printStackTrace()
            null
        }
    }
}