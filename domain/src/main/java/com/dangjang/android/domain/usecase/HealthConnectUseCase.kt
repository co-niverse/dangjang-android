package com.dangjang.android.domain.usecase

import android.util.Log
import com.dangjang.android.domain.model.GetLastDateVO
import com.dangjang.android.domain.model.IntroVO
import com.dangjang.android.domain.repository.HealthConnectRepository
import com.dangjang.android.domain.request.HealthConnectRequest
import com.dangjang.android.domain.request.PatchHealthConnectRequest
import com.dangjang.android.domain.request.PostHealthConnectRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HealthConnectUseCase @Inject constructor(
    private val healthConnectRepository: HealthConnectRepository
) {
    suspend fun getIntro(): Flow<IntroVO> =
        withContext(Dispatchers.IO) {
            healthConnectRepository.getIntroApi()
        }

    suspend fun postHealthConnect(accessToken: String, postHealthConnectRequest: PostHealthConnectRequest): Flow<Boolean> =
        withContext(Dispatchers.IO) {
            var postHealthConnectRequestNotNull = mutableListOf<HealthConnectRequest>()
            postHealthConnectRequest.data.forEach {
                if (it != HealthConnectRequest("","","")) {
                    postHealthConnectRequestNotNull.add(it)
                }
            }
            Log.e("postHealthConnectRequestNotNull",postHealthConnectRequestNotNull.toString())
            healthConnectRepository.postHealthConnect(accessToken, PostHealthConnectRequest(postHealthConnectRequestNotNull))
        }

    suspend fun patchHealthConnectInterlock(accessToken: String, patchHealthConnectRequest: PatchHealthConnectRequest): Flow<Boolean> =
        withContext(Dispatchers.IO) {
            healthConnectRepository.patchHealthConnectInterlock("Bearer $accessToken", patchHealthConnectRequest)
        }

    suspend fun getHealthMetricLastDate(accessToken: String): Flow<GetLastDateVO> =
        withContext(Dispatchers.IO) {
            healthConnectRepository.getHealthMetricLastDate("Bearer $accessToken")
        }
}