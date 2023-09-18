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
import com.dangjang.android.domain.request.HealthConnectRequest
import com.dangjang.android.domain.request.PostHealthConnectRequest
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
import java.time.Duration
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

    private var healthConnectList = mutableListOf<HealthConnectRequest>()

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
    fun getWeightHealthConnect() {
        viewModelScope.launch {
            //TODO: 여기가 문제임. 이 함수들이 전부 다 실행되지 않는다.
            tryWithWeightPermissionsCheck()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getGlucoseHealthConnect() {
        viewModelScope.launch {
            tryWithBloodGlucosePermissionsCheck()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStepsHealthConnect() {
        viewModelScope.launch {
            tryWithStepsPermissionCheck()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getExerciseHealthConnect() {
        viewModelScope.launch {
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
            Log.e("실행 되는지","실행 되는가 ??")
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
            //TODO : 날짜 처리
            healthConnectList.add(HealthConnectRequest(changeInstantToKSTDate(weightRecord.time),"체중",weightRecord.weight.toString()))
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
            var relationToMeal = RELATION_TO_MEAL_INT_TO_STRING_MAP.get(bloodGlucoseRecord.relationToMeal)
            Log.e("HC-BloodGlucose",bgTime + "시: " + "("+ mealType + ", "+relationToMeal + ") " + bloodGlucoseRecord.level.inMilligramsPerDeciliter.roundToInt() + "mg/dL" )

            when(relationToMeal) {
                "general" -> {
                    relationToMeal = "기타"
                }
                "fasting" -> {
                    relationToMeal = "공복"
                }
                "before_meal" -> {
                    when (bgTime.substring(0,2).toInt()) {
                        in 0..10 -> {
                            relationToMeal = "아침식전"
                        }
                        in 11..15 -> {
                            relationToMeal = "점심식전"
                        }
                        in 16..23 -> {
                            relationToMeal = "저녁식전"
                        }
                    }
                }
                "after_meal" -> {
                    when (bgTime.substring(0,2).toInt()) {
                        in 0..10 -> {
                            relationToMeal = "아침식후"
                        }
                        in 11..15 -> {
                            relationToMeal = "점심식후"
                        }
                        in 16..23 -> {
                            relationToMeal = "저녁식후"
                        }
                    }
                }
            }

            healthConnectList.add(HealthConnectRequest(changeInstantToKSTDate(bloodGlucoseRecord.time), relationToMeal ?: "기타", bloodGlucoseRecord.level.inMilligramsPerDeciliter.roundToInt().toString()))
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
            //TODO : 날짜 처리
            healthConnectList.add(HealthConnectRequest(changeInstantToKSTDate(stepsRecord.startTime),"걸음수",stepsRecord.count.toString()))
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
            var exerciseName = ExerciseSessionRecord.EXERCISE_TYPE_INT_TO_STRING_MAP.get(exerciseRecord.exerciseType)
            val exerciseStartTime = changeInstantToKST(exerciseRecord.startTime)
            val exerciseEndTime = changeInstantToKST(exerciseRecord.endTime)

            val duration = Duration.between(exerciseRecord.startTime, exerciseRecord.endTime).toMinutes()

            Log.e("HC-Exercise", "운동 종류: $exerciseName 시간: $exerciseStartTime to $exerciseEndTime")

            exerciseName = when (exerciseName) {
                "walking"-> "걷기"
                "running" -> "달리기"
                "hiking" -> "하이킹"
                "biking" -> "자전거"
                "swimming_open_water" -> "수영"
                "swimming_pool" -> "수영"
                "workout" -> "헬스"
                "barbell_shoulder_press" -> "헬스"
                "bench_press" -> "헬스"
                "bench_sit_up" -> "헬스"
                "deadlift" -> "헬스"
                "lunge" -> "헬스"
                "plank" -> "헬스"
                "dumbbell_curl_left_arm" -> "헬스"
                "dumbbell_curl_right_arm" -> "헬스"
                "dumbbell_front_raise" -> "헬스"
                "dumbbell_lateral_raise" -> "헬스"
                "dumbbell_triceps_extension_left_arm" -> "헬스"
                "dumbbell_triceps_extension_right_arm" -> "헬스"
                "dumbbell_triceps_extension_two_arm" -> "헬스"
                 else -> "제외"
            }

            if (exerciseName != "제외") {
                //TODO : 날짜 처리 & 운동명 처리 & 운동시간 처리
                healthConnectList.add(
                    HealthConnectRequest(
                        changeInstantToKSTDate(exerciseRecord.startTime),
                        exerciseName,
                        duration.toString()
                    )
                )
            }
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

    fun postHealthConnectData(accessToken: String) {
        viewModelScope.launch {
            splashUseCase.postHealthConnect("Bearer $accessToken", PostHealthConnectRequest(healthConnectList))
                .handleErrors()
                .collect()
        }
    }

    private fun <T> Flow<T>.handleErrors(): Flow<T> =
        catch { e -> Toast.makeText(getApplication<Application>().applicationContext,e.message,Toast.LENGTH_SHORT).show() }

    private fun getTodayStartTime(): Instant {
        val koreaZoneId = ZoneId.of("Asia/Seoul")
        val koreaTime = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).toInstant().atZone(koreaZoneId)

        return koreaTime.toInstant()
    }

    private fun getNowTime(): Instant {
        val koreaZoneId = ZoneId.of("Asia/Seoul")
        val koreaTime = ZonedDateTime.now().toInstant().atZone(koreaZoneId)

        return koreaTime.toInstant()
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

    private fun changeInstantToKSTDate(instant: Instant): String {
        val koreaZoneId = ZoneId.of("Asia/Seoul")
        val koreaTime = instant.atZone(koreaZoneId)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDateTime = koreaTime.format(formatter)

        return formattedDateTime
    }
}