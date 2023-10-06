package com.dangjang.android.data.repository

import com.dangjang.android.data.datasource.MypageDataSource
import com.dangjang.android.domain.model.GetMypageVO
import com.dangjang.android.domain.model.GetPointVO
import com.dangjang.android.domain.model.PostPointVO
import com.dangjang.android.domain.repository.MypageRepository
import com.dangjang.android.domain.request.PostPointRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MypageRepositoryImpl @Inject constructor(
    private val mypageDataSource: MypageDataSource
) : MypageRepository {

    override fun getMypage(accessToken: String): Flow<GetMypageVO> = flow {
        val response = mypageDataSource.getMypage(accessToken)
        emit(response.data.toDomain())
    }

    override fun getPoint(accessToken: String): Flow<GetPointVO> = flow {
        val response = mypageDataSource.getPoint(accessToken)
        emit(response.data.toDomain())
    }

    override fun postPoint(accessToken: String, postPointRequest: PostPointRequest): Flow<PostPointVO> = flow {
        val response = mypageDataSource.postPoint(accessToken, postPointRequest)
        emit(response.data.toDomain())
    }
}