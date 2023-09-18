package com.dangjang.android.data.repository

import com.dangjang.android.data.datasource.ChartDataSource
import com.dangjang.android.domain.model.GetChartVO
import com.dangjang.android.domain.repository.ChartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChartRepositoryImpl @Inject constructor(
    private val chartDataSource: ChartDataSource
) : ChartRepository {

    override fun getChart(accessToken: String, startDate: String, endDate: String) : Flow<GetChartVO> = flow {
        val response = chartDataSource.getChart(accessToken, startDate, endDate)
        emit(response.data.toDomain())
    }
}