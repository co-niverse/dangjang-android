package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.response.BaseResponse
import com.dangjang.android.data.sdui.SduiSignupDto
import javax.inject.Inject

class SduiDataSource @Inject constructor(
    private val sduiApiService: SduiApiService
) : BaseNetworkDataSource() {

    suspend fun getSduiSignup() : BaseResponse<SduiSignupDto> {
        return checkResponse(sduiApiService.getSduiSignup())
    }

}