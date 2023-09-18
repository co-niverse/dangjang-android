package com.dangjang.android.domain.usecase

import com.dangjang.android.domain.model.GetChartVO
import com.dangjang.android.domain.repository.ChartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChartUseCase @Inject constructor(
    private val chartRepository: ChartRepository
) {
    suspend fun getChart(
        accessToken: String,
        startDate: String,
        endDate: String
    ): Flow<GetChartVO> =
        withContext(Dispatchers.IO) {
            chartRepository.getChart(accessToken, startDate, endDate)
        }
}