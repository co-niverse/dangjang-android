package com.dangjang.android.presentation.intro

import  android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.AUTO_LOGIN_EDITOR_KEY
import com.dangjang.android.domain.constants.AUTO_LOGIN_SPF_KEY
import com.dangjang.android.domain.constants.HEALTH_CONNECT_INSTALLED
import com.dangjang.android.domain.constants.HEALTH_CONNECT_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.model.GetLastDateVO
import com.dangjang.android.domain.model.HealthConnectVO
import com.dangjang.android.domain.model.IntroVO
import com.dangjang.android.domain.request.HealthConnectRequest
import com.dangjang.android.domain.request.PatchHealthConnectRequest
import com.dangjang.android.domain.request.PostHealthConnectRequest
import com.dangjang.android.domain.usecase.HealthConnectUseCase
import com.dangjang.android.domain.usecase.TokenUseCase
import com.dangjang.android.presentation.login.LoginActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class HealthConnectViewModel @Inject constructor(
    private val healthConnectUseCase: HealthConnectUseCase,
    private val getTokenUseCase: TokenUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _introDataFlow = MutableStateFlow(IntroVO())
    val introDataFlow = _introDataFlow.asStateFlow()

    private val _healthMetricLastDate = MutableStateFlow(GetLastDateVO())
    val healthMetricLastDate = _healthMetricLastDate.asStateFlow()

    private val _patchHealthConnectInterlockFlow = MutableStateFlow("none")
    val patchHealthConnectInterlockFlow = _patchHealthConnectInterlockFlow.asStateFlow()

    private val _healthConnectFlow = MutableStateFlow(HealthConnectVO())
    val healthConnectFlow = _healthConnectFlow.asStateFlow()

    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(getApplication<Application>().applicationContext) }

    private val _postHealthConnectFlow = MutableStateFlow(false)
    val postHealthConnectFlow = _postHealthConnectFlow.asStateFlow()

    private val _healthConnectList = MutableStateFlow(mutableListOf<HealthConnectRequest>())
    val healthConnectList = _healthConnectList.asStateFlow()

    private val _hcWeightList = MutableStateFlow(mutableListOf<HealthConnectRequest>())
    val hcWeightList = _hcWeightList.asStateFlow()

    private val _hcGlucoseList = MutableStateFlow(mutableListOf<HealthConnectRequest>())
    val hcGlucoseList = _hcGlucoseList.asStateFlow()

    private val _hcStepList = MutableStateFlow(mutableListOf<HealthConnectRequest>())
    val hcStepList = _hcStepList.asStateFlow()

    private val _hcExerciseList = MutableStateFlow(mutableListOf<HealthConnectRequest>())
    val hcExerciseList = _hcExerciseList.asStateFlow()

    private val _hcExerciseGlucoseList = MutableStateFlow(mutableListOf<HealthConnectRequest>())
    val hcExerciseGlucoseList = _hcExerciseGlucoseList.asStateFlow()

    private val _hcWeightStepList = MutableStateFlow(mutableListOf<HealthConnectRequest>())
    val hcWeightStepList = _hcWeightStepList.asStateFlow()

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

    private val _checkHealthConnectInterlock = MutableStateFlow("none")
    val checkHealthConnectInterlock = _checkHealthConnectInterlock.asStateFlow()

    private val _reissueTokenFlow = MutableStateFlow(false)
    val reissueTokenFlow = _reissueTokenFlow.asStateFlow()

    fun getHealthMetricLastDate(accessToken: String) {
        viewModelScope.launch {
            healthConnectUseCase.getHealthMetricLastDate(accessToken)
                .onEach {
                    _healthMetricLastDate.emit(it)
                    Log.e("최근 로그인 날짜",it.date)
                }
                .handleErrors()
                .collect()
        }
    }

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

    fun checkHealthConnectInterlock() {
        viewModelScope.launch {
            checkHealthConnectInterlock.collectLatest {
                if (it == "true") {
                    if (healthConnectFlow.value.isAvaiable == HEALTH_CONNECT_INSTALLED) {
                        patchHealthConnectInterlock(getAccessToken() ?: "", PatchHealthConnectRequest(true))
                    } else {
                        patchHealthConnectInterlock(getAccessToken() ?: "", PatchHealthConnectRequest(false))
                    }
                } else if (it == "false") {
                    patchHealthConnectInterlock(getAccessToken() ?: "", PatchHealthConnectRequest(false))
                }
            }
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
            _checkHealthConnectInterlock.update { "true" }
        } else {
            Log.e("GRANT-ERROR","체중 권한이 허용되지 않았습니다.")
            _checkHealthConnectInterlock.update { "false" }
        }
    }

    private suspend fun tryWithBloodGlucosePermissionsCheck() {
        bloodGlucosePermissionGranted = hasAllPermissions(bloodGlucosePermission)
        if (bloodGlucosePermissionGranted) {
            readBloodGlucose()
            _checkHealthConnectInterlock.update { "true" }
        } else {
            Log.e("GRANT-ERROR","혈당 권한이 허용되지 않았습니다.")
            _checkHealthConnectInterlock.update { "false" }
        }
    }

    private suspend fun tryWithStepsPermissionCheck() {
        stepsPermissionGranted = hasAllPermissions(stepsPermission)
        if (stepsPermissionGranted) {
            readSteps()
            _checkHealthConnectInterlock.update { "true" }
        } else {
            Log.e("GRANT-ERROR","걸음수 권한이 허용되지 않았습니다.")
            _checkHealthConnectInterlock.update { "false" }
        }
    }

    private suspend fun tryWithExerciseSessionPermissionCheck() {
        exerciseSessionPermissionGranted = hasAllPermissions(exerciseSessionPermission)
        if (exerciseSessionPermissionGranted) {
            readExerciseSession()
            _checkHealthConnectInterlock.update { "true" }
        } else {
            Log.e("GRANT-ERROR","운동 권한이 허용되지 않았습니다.")
            _checkHealthConnectInterlock.update { "false" }
        }
    }

    suspend fun hasAllPermissions(permissions: Set<String>): Boolean { //허가 받기
        return healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)
    }

    //체중
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun readWeight() {
        val hcWeightTestList = mutableListOf<HealthConnectRequest>()
        //TODO : 로그인 시작 시간 처리 후 getTodayStartTime() -> getStartTime() 함수로 대치
        weightList = readWeightRecord(getStartTime() ?: getTodayStartTime(), getNowTime())
        for (weightRecord in weightList) {
            Log.e("HC-Weight",weightRecord.weight.toString())
            hcWeightTestList.apply {
                    add(HealthConnectRequest(
                        changeInstantToKSTDate(weightRecord.time),
                        "체중",
                        weightRecord.weight.inKilograms.toInt().toString()
                    ))
                }
        }
        if (hcWeightTestList.isEmpty()) {
            _hcWeightList.update {
                mutableListOf(HealthConnectRequest("","",""))
            }
        } else {
            _hcWeightList.update {
                hcWeightTestList
            }
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
        Log.e("readBloodGlucose","readBloodGlucose 실행 첫번째 줄")
        val hcGlucoseTestList = mutableListOf<HealthConnectRequest>()
        bloodGlucoseList = readBloodGlucoseRecord(getStartTime() ?: getTodayStartTime(), getNowTime())
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

            hcGlucoseTestList.apply {
                    add(HealthConnectRequest(changeInstantToKSTDate(bloodGlucoseRecord.time), relationToMeal ?: "기타", bloodGlucoseRecord.level.inMilligramsPerDeciliter.roundToInt().toString()))
                }
        }
        Log.e("readBloodGlucose - hcGlucoseTestList",hcGlucoseTestList.toString())
        if (hcGlucoseTestList.isEmpty()) {
            _hcGlucoseList.update {
                mutableListOf(HealthConnectRequest("","",""))
            }
        } else {
            _hcGlucoseList.update {
                hcGlucoseTestList
            }
        }
    }

    private suspend fun readBloodGlucoseRecord(start: Instant, end: Instant): List<BloodGlucoseRecord> {
        Log.e("readBloodGlucoseRecord","readBloodGlucoseRecord 실행됨")
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
        val hcStepsTestList = mutableListOf<HealthConnectRequest>()
        stepList = readStepsRecord(getStartTime() ?: getTodayStartTime(), getNowTime())
        for (stepsRecord in stepList) {
            Log.e("HC-Steps",stepsRecord.count.toString()+"보")
            hcStepsTestList.apply {
                    add(
                    HealthConnectRequest(changeInstantToKSTDate(stepsRecord.startTime),"걸음수",stepsRecord.count.toString())
                    )
                }
       }
        if (hcStepsTestList.isEmpty()) {
            _hcStepList.update {
                mutableListOf(HealthConnectRequest("","",""))
            }
        } else {
            _hcStepList.update {
                hcStepsTestList
            }
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
        val hcExerciseTestList = mutableListOf<HealthConnectRequest>()
        exerciseList = readExerciseSessionRecord(getStartTime() ?: getTodayStartTime(),getNowTime())
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
                hcExerciseTestList.apply {
                        add(HealthConnectRequest(
                            changeInstantToKSTDate(exerciseRecord.startTime),
                            exerciseName,
                            duration.toString()
                        ))
                    }
            }
        }
        Log.e("운동 완료",healthConnectList.value.toString())
        if (hcExerciseTestList.isEmpty()) {
            _hcExerciseList.update {
                mutableListOf(HealthConnectRequest("","",""))
            }
        } else {
            _hcExerciseList.update {
                hcExerciseTestList
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
            healthConnectUseCase.getIntro()
                .onEach {
                    _introDataFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

    private fun patchHealthConnectInterlock(accessToken: String, patchHealthConnectRequest: PatchHealthConnectRequest) {
        viewModelScope.launch {
            healthConnectUseCase.patchHealthConnectInterlock(accessToken, patchHealthConnectRequest)
                .onEach {
                    if (it) {
                        _patchHealthConnectInterlockFlow.emit(checkHealthConnectInterlock.value)
                    }
                }
                .handleErrors()
                .collect()
        }
    }

    fun setHcExerciseGlucoseList(list: List<HealthConnectRequest>) {
        _hcExerciseGlucoseList.value = list.toMutableList()
    }

    fun setHcWeightStepList(list: List<HealthConnectRequest>) {
        _hcWeightStepList.value = list.toMutableList()
    }

    fun getAllHealthConnectData() {
        getGlucoseHealthConnect()
        getWeightHealthConnect()
        getStepsHealthConnect()
        getExerciseHealthConnect()
    }


    fun postHealthConnectData(accessToken: String) {
        _healthConnectList.value.addAll(hcExerciseGlucoseList.value)
        _healthConnectList.value.addAll(hcWeightStepList.value)
        viewModelScope.launch {
            Log.e("hc 등록 테스트",healthConnectList.value.toString())
            healthConnectUseCase.postHealthConnect("Bearer $accessToken", PostHealthConnectRequest(data = healthConnectList.value))
                .onEach {
                    _postHealthConnectFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

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

    private fun getStartTime(): Instant? {
        if (healthMetricLastDate.value.date == "") {
            return getTodayStartTime()
        } else {
            return convertDateStirngToInstant(healthMetricLastDate.value.date)
        }
    }

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

    private fun convertDateStirngToInstant(dateString: String): Instant? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        try {
            val date = dateFormat.parse(dateString)
            return date?.toInstant()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun changeInstantToKSTDate(instant: Instant): String {
        val koreaZoneId = ZoneId.of("Asia/Seoul")
        val koreaTime = instant.atZone(koreaZoneId)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDateTime = koreaTime.format(formatter)

        return formattedDateTime
    }

    fun getHealtConnectSpf(): String? {
        val sp: SharedPreferences = getApplication<Application>().applicationContext.getSharedPreferences(
            AUTO_LOGIN_SPF_KEY,
            AppCompatActivity.MODE_PRIVATE
        )
        val provider = sp.getString(AUTO_LOGIN_EDITOR_KEY, "null")
        Log.e("sp", provider.toString())

        return sp.getString(HEALTH_CONNECT_TOKEN_KEY, "null")
    }

    fun getAutoLoginProviderSpf(): String? {
        val sp: SharedPreferences = getApplication<Application>().applicationContext.getSharedPreferences(
            AUTO_LOGIN_SPF_KEY,
            AppCompatActivity.MODE_PRIVATE
        )
        return sp.getString(AUTO_LOGIN_EDITOR_KEY, "null")
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = getApplication<Application>().applicationContext.getSharedPreferences(
            TOKEN_SPF_KEY, Context.MODE_PRIVATE)

        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    private fun <T> Flow<T>.handleErrors(): Flow<T> =
        catch { e ->
            Log.e("error",e.message.toString())
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
                Intent(getApplication<Application>().applicationContext, LoginActivity::class.java).apply {
                    getApplication<Application>().applicationContext.startActivity(this)
                }
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