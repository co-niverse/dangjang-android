package com.dangjang.android.presentation

import android.app.Application
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.Energy
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
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.roundToInt

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

    lateinit var weightList: List<WeightRecord>
    lateinit var stepList: List<StepsRecord>
    lateinit var exerciseList: List<ExerciseSessionRecord>
    lateinit var sleepDuration: Duration
    lateinit var sleepSessionList: List<SleepSessionRecord>
    lateinit var bloodGlucoseList: List<BloodGlucoseRecord>
    lateinit var bloodPressureList: List<BloodPressureRecord>

    private val weightPermission = setOf(
        HealthPermission.getReadPermission(WeightRecord::class)
    )
    private val bloodGlucosePermission = setOf(
        HealthPermission.getReadPermission(BloodGlucoseRecord::class)
    )
    private val bloodPressurePermission = setOf(
        HealthPermission.getReadPermission(BloodPressureRecord::class)
    )
    private val sleepSessionPermission = setOf(
        HealthPermission.getReadPermission(SleepSessionRecord::class)
    )
    private val stepsPermission = setOf(
        HealthPermission.getReadPermission(StepsRecord::class)
    )
    private val activeCaloriesBurnedPermission = setOf(
        HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class)
    )
    private val exerciseSessionPermission = setOf(
        HealthPermission.getReadPermission(ExerciseSessionRecord::class)
    )

    var weightPermissionGranted = false
    var bloodGlucosePermissionGranted = false
    var bloodPressurePermissionGranted = false
    var sleepSessionPermissionGranted = false
    var stepsPermissionGranted = false
    var activeCaloriesBurnedPermissionGranted = false
    var exerciseSessionPermissionGranted = false

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHealthConnect() {
        viewModelScope.launch {
            tryWithWeightPermissionsCheck()
            tryWithBloodGlucosePermissionsCheck()
            tryWithBloodPressurePermissionCheck()
            tryWithSleepSessionPermissionCheck()
            tryWithStepsPermissionCheck()
            tryWithActiveCaloriesBurnedPermissionCheck()
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

    private suspend fun tryWithBloodPressurePermissionCheck() {
        bloodPressurePermissionGranted = hasAllPermissions(bloodPressurePermission)
        if (bloodPressurePermissionGranted) {
            readBloodPressureRecord()
        } else {
            Log.e("GRANT-ERROR","혈압 권한이 허용되지 않았습니다.")
        }
    }

    private suspend fun tryWithSleepSessionPermissionCheck() {
        sleepSessionPermissionGranted = hasAllPermissions(sleepSessionPermission)
        if (sleepSessionPermissionGranted) {
            readSleepSession()
        } else {
            Log.e("GRANT-ERROR","수면 권한이 허용되지 않았습니다.")
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

    private suspend fun tryWithActiveCaloriesBurnedPermissionCheck() {
        activeCaloriesBurnedPermissionGranted = hasAllPermissions(activeCaloriesBurnedPermission)
        if (activeCaloriesBurnedPermissionGranted) {
            readActiveCaloriesBurned()
        } else {
            Log.e("GRANT-ERROR","활동 칼로리 소모량 권한이 허용되지 않았습니다.")
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
        val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val endOfWeek = startOfDay.toInstant().plus(7,ChronoUnit.DAYS)

        weightList = readWeightInput(startOfDay.toInstant(),endOfWeek)
        for (weightRecord in weightList) {
            Log.e("HC-Weight",weightRecord.weight.toString())
        }
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

        bloodGlucoseList = readBloodGlucoseInput(startOfDay.toInstant(),endOfWeek)
        for (bloodGlucoseRecord in bloodGlucoseList) {
            val bgTime = changeInstantToKST(bloodGlucoseRecord.time)
            Log.e("HC-BloodGlucose",bgTime + "시: " + bloodGlucoseRecord.level.inMilligramsPerDeciliter.roundToInt() + "mg/dL" )
        }
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

        bloodPressureList = readBloodPressureInput(startOfDay.toInstant(),endOfWeek)
        for (bloodPressureRecord in bloodPressureList) {
            //시간
            val bpTime = changeInstantToKST(bloodPressureRecord.time)
            //수축기
            val systolicRecord = bloodPressureRecord.systolic.inMillimetersOfMercury.roundToInt()
            //이완기
            val diastolicRecord = bloodPressureRecord.diastolic.inMillimetersOfMercury.roundToInt()
            Log.e("HC-BloodPressure", "시간: $bpTime, 수축기: $systolicRecord, 이완기: $diastolicRecord")
        }
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
        val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val yesterdayNight = startOfDay.toInstant().minus(Duration.ofHours(6)) // 저녁 6시부터로 설정해놓음
        val endOfWeek = startOfDay.toInstant().plus(7,ChronoUnit.DAYS)

        sleepSessionList = readSleepSessionInput(yesterdayNight,endOfWeek)
        for (sleepSessionRecord in sleepSessionList) {
            val sleepStartTime = changeInstantToKST(sleepSessionRecord.startTime)
            val sleepEndTime = changeInstantToKST(sleepSessionRecord.endTime)
            Log.e("HC-Sleep","시작 시간: $sleepStartTime, 종료 시간: $sleepEndTime")
        }

        sleepDuration = readSleepDuration(yesterdayNight,endOfWeek)!!
        val durationHours: String = sleepDuration.toHours().toString()
        val durationMinutes: String = (sleepDuration.toMinutes() % 60).toString()
        val durationSeconds: String = (sleepDuration.getSeconds() % 60).toString()
        Log.e("HC-SleepDuration",durationHours + "시간 " + durationMinutes+"분 "+durationSeconds+"초")
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
    private suspend fun readSleepDuration(start: Instant, end: Instant): Duration? {
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
        stepList = readStepsInput(startOfDay.toInstant(),endOfWeek)
        for (stepsRecord in stepList) {
            Log.e("HC-Steps",stepsRecord.count.toString()+"보")
        }
    }

    private suspend fun readStepsInput(start: Instant, end: Instant): List<StepsRecord> {
        val request = ReadRecordsRequest(
            recordType = StepsRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }

    //소모 칼로리
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun readActiveCaloriesBurned() {
        val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val endOfWeek = startOfDay.toInstant().plus(7,ChronoUnit.DAYS)
        val calories = readActiveCaloriesBurnedInput(startOfDay.toInstant(),endOfWeek)
        Log.e("HC-Calories",calories?.inCalories.toString()+"cal")
    }

    private suspend fun readActiveCaloriesBurnedInput(start: Instant, end: Instant): Energy? {
        val request = AggregateRequest(
            metrics = setOf(ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL),
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.aggregate(request)
        return response[ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL]
    }

    //운동
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun readExerciseSession() {
        val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val endOfWeek = startOfDay.toInstant().plus(7,ChronoUnit.DAYS)

        exerciseList = readExerciseSessionInput(startOfDay.toInstant(),endOfWeek)
        for (exerciseRecord in exerciseList) {
            val exerciseName = ExerciseSessionRecord.EXERCISE_TYPE_INT_TO_STRING_MAP.get(exerciseRecord.exerciseType)
            val exerciseStartTime = changeInstantToKST(exerciseRecord.startTime)
            val exerciseEndTime = changeInstantToKST(exerciseRecord.endTime)
            Log.e("HC-Exercise", "운동 종류: $exerciseName 시간: $exerciseStartTime to $exerciseEndTime")
        }
    }

    private suspend fun readExerciseSessionInput(start: Instant, end: Instant): List<ExerciseSessionRecord> {
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
            getIntroUseCase.getIntro().collect{
                if (it is Exception)
                    Toast.makeText(getApplication<Application>().applicationContext,it.message,Toast.LENGTH_SHORT).show()
                if (it is IntroVO)
                    _introDataFlow.emit(it)
            }
        }
    }

}