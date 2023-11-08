package com.dangjang.android.data.repository

import com.dangjang.android.data.datasource.SduiDataSource
import com.dangjang.android.domain.repository.SduiRepository
import com.dangjang.android.domain.sdui.SduiSignupVO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SduiRepositoryImpl @Inject constructor(
    private val sduiDataSource: SduiDataSource
) : SduiRepository {

    override fun getSduiSignup(): Flow<SduiSignupVO> = flow {
        val response = sduiDataSource.getSduiSignup()
        emit(response.data.toDomain())
    }
}