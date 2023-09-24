package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.GetChartVO
import kotlinx.coroutines.flow.Flow

interface ChartRepository {
    //건강차트 조회
    fun getChart(accessToken: String, startDate: String, endDate: String): Flow<GetChartVO>
}