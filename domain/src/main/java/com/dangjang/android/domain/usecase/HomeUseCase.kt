package com.dangjang.android.domain.usecase

import com.dangjang.android.domain.model.GetGlucoseVO
import com.dangjang.android.domain.model.EditHealthMetricVO
import com.dangjang.android.domain.model.PostPatchExerciseVO
import com.dangjang.android.domain.model.PostPatchWeightVO
import com.dangjang.android.domain.repository.HomeRepository
import com.dangjang.android.domain.request.AddHealthMetricRequest
import com.dangjang.android.domain.request.EditHealthMetricRequest
import com.dangjang.android.domain.request.EditSameHealthMetricRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeUseCase @Inject constructor(
    private val homeRepository: HomeRepository
){
    suspend fun addHealthMetric(
        accessToken: String,
        addHealthMetricRequest: AddHealthMetricRequest
    ): Flow<EditHealthMetricVO> =
        withContext(Dispatchers.IO) {
        homeRepository.addHealthMetric(accessToken, addHealthMetricRequest)
    }

    suspend fun getGlucose(
        accessToken: String,
        date: String
    ): Flow<GetGlucoseVO> =
        withContext(Dispatchers.IO) {
            homeRepository.getGlucose(accessToken, date)
        }

    suspend fun editGlucose(
        accessToken: String,
        editHealthMetricRequest: EditHealthMetricRequest
    ): Flow<EditHealthMetricVO> =
        withContext(Dispatchers.IO) {
            homeRepository.editGlucose(accessToken, editHealthMetricRequest)
        }

    suspend fun editSameGlucose(
        accessToken: String,
        editSameHealthMetricRequest: EditSameHealthMetricRequest
    ): Flow<EditHealthMetricVO> =
        withContext(Dispatchers.IO) {
            homeRepository.editSameGlucose(accessToken, editSameHealthMetricRequest)
        }

    suspend fun addWeight(
        accessToken: String,
        addHealthMetricRequest: AddHealthMetricRequest
    ): Flow<PostPatchWeightVO> =
        withContext(Dispatchers.IO) {
            homeRepository.addWeight(accessToken, addHealthMetricRequest)
        }

    suspend fun editWeight(
        accessToken: String,
        editSameHealthMetricRequest: EditSameHealthMetricRequest
    ): Flow<PostPatchWeightVO> =
        withContext(Dispatchers.IO) {
            homeRepository.editWeight(accessToken, editSameHealthMetricRequest)
        }

    suspend fun addExercise(
        accessToken: String,
        addHealthMetricRequest: AddHealthMetricRequest
    ): Flow<PostPatchExerciseVO> =
        withContext(Dispatchers.IO) {
            homeRepository.addExercise(accessToken, addHealthMetricRequest)
        }

    suspend fun editExercise(
        accessToken: String,
        editSameHealthMetricRequest: EditSameHealthMetricRequest
    ): Flow<PostPatchExerciseVO> =
        withContext(Dispatchers.IO) {
            homeRepository.editExercise(accessToken, editSameHealthMetricRequest)
        }
    
}