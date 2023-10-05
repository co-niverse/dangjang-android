package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.GetMypageDto
import com.dangjang.android.data.model.response.BaseResponse
import javax.inject.Inject

class MypageDataSource @Inject constructor(
    private val mypageApiService: MypageApiService
): BaseNetworkDataSource() {

    suspend fun getMypage(accessToken: String): BaseResponse<GetMypageDto> {
        return checkResponse(mypageApiService.getMypage(accessToken))
    }
}