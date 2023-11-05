package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.GetGlucoseVO
import com.dangjang.android.domain.model.EditHealthMetricVO
import com.dangjang.android.domain.model.GetExerciseVO
import com.dangjang.android.domain.model.GetHomeVO
import com.dangjang.android.domain.model.GetNotificationVO
import com.dangjang.android.domain.model.GetWeightVO
import com.dangjang.android.domain.model.PostPatchExerciseVO
import com.dangjang.android.domain.model.PostPatchWeightVO
import com.dangjang.android.domain.request.AddHealthMetricRequest
import com.dangjang.android.domain.request.EditHealthMetricRequest
import com.dangjang.android.domain.request.EditSameHealthMetricRequest
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    //홈 조회
    fun getHome(accessToken: String, date: String): Flow<GetHomeVO>

    //알람 목록 조회
    fun getNotification(accessToken: String): Flow<GetNotificationVO>

    //알람 확인 체크
    fun checkNotification(accessToken: String, notificationIdList: List<Int>): Flow<Boolean>

    //건강지표 등록
    fun addHealthMetric(accessToken: String, addHealthMetricRequest: AddHealthMetricRequest): Flow<EditHealthMetricVO>

    //혈당 조회
    fun getGlucose(accessToken: String, date: String): Flow<GetGlucoseVO>

    //혈당 수정
    fun editGlucose(accessToken: String, editHealthMetricRequest: EditHealthMetricRequest): Flow<EditHealthMetricVO>

    //혈당 수정 (같은 시간 tyep일 경우)
    fun editSameGlucose(accessToken: String, editSameHealthMetricRequest: EditSameHealthMetricRequest): Flow<EditHealthMetricVO>

    //체중 조회
    fun getWeight(accessToken: String, date: String): Flow<GetWeightVO>

    //체중 추가
    fun addWeight(accessToken: String, addHealthMetricRequest: AddHealthMetricRequest): Flow<PostPatchWeightVO>

    //체중 수정
    fun editWeight(accessToken: String, editSameHealthMetricRequest: EditSameHealthMetricRequest): Flow<PostPatchWeightVO>

    //운동 조회
    fun getExercise(accessToken: String, date: String): Flow<GetExerciseVO>

    //운동 추가
    fun addExercise(accessToken: String, addHealthMetricRequest: AddHealthMetricRequest): Flow<PostPatchExerciseVO>

    //운동 수정
    fun editExercise(accessToken: String, editSameHealthMetricRequest: EditSameHealthMetricRequest): Flow<PostPatchExerciseVO>

    fun deleteHealthMetric(accessToken: String, date: String, type: String): Flow<Boolean>
}