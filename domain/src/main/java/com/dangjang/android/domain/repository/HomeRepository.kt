package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.GetGlucoseVO
import com.dangjang.android.domain.model.EditHealthMetricVO
import com.dangjang.android.domain.model.PostPatchWeightVO
import com.dangjang.android.domain.request.AddHealthMetricRequest
import com.dangjang.android.domain.request.EditHealthMetricRequest
import com.dangjang.android.domain.request.EditSameHealthMetricRequest
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    //건강지표 등록
    fun addHealthMetric(accessToken: String, addHealthMetricRequest: AddHealthMetricRequest): Flow<EditHealthMetricVO>

    //혈당 조회
    fun getGlucose(accessToken: String, date: String): Flow<GetGlucoseVO>

    //혈당 수정
    fun editGlucose(accessToken: String, editHealthMetricRequest: EditHealthMetricRequest): Flow<EditHealthMetricVO>

    //혈당 수정 (같은 시간 tyep일 경우)
    fun editSameGlucose(accessToken: String, editSameHealthMetricRequest: EditSameHealthMetricRequest): Flow<EditHealthMetricVO>

    //체중 추가
    fun addWeight(accessToken: String, addHealthMetricRequest: AddHealthMetricRequest): Flow<PostPatchWeightVO>

    //체중 수정
    fun editWeight(accessToken: String, editSameHealthMetricRequest: EditSameHealthMetricRequest): Flow<PostPatchWeightVO>
}