package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.IntroDto
import com.dangjang.android.data.model.response.BaseResponse
import javax.inject.Inject

class IntroDataSource @Inject constructor(
    private val introDataSourceImpl: IntroDataSourceImpl
) : BaseNetworkDataSource() {

    suspend fun getIntroApi(): BaseResponse<IntroDto> {
        return checkResponse(introDataSourceImpl.getIntroApi())
    }
}