package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.GetMypageDto
import com.dangjang.android.data.model.dto.GetPointDto
import com.dangjang.android.data.model.dto.PostPointDto
import com.dangjang.android.data.model.response.BaseResponse
import com.dangjang.android.domain.request.PostPointRequest
import javax.inject.Inject

class MypageDataSource @Inject constructor(
    private val mypageApiService: MypageApiService
): BaseNetworkDataSource() {

    suspend fun getMypage(accessToken: String): BaseResponse<GetMypageDto> {
        return checkResponse(mypageApiService.getMypage(accessToken))
    }

    suspend fun getPoint(accessToken: String): BaseResponse<GetPointDto> {
        return checkResponse(mypageApiService.getPoint(accessToken))
    }

    suspend fun postPoint(accessToken: String, postPointRequest: PostPointRequest): BaseResponse<PostPointDto> {
        return checkResponse(mypageApiService.postPoint(accessToken, postPointRequest))
    }

    suspend fun signout(accessToken: String): BaseResponse<Nothing> {
        return checkResponse(mypageApiService.signout(accessToken))
    }
}