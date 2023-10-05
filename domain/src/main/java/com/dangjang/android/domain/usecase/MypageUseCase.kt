package com.dangjang.android.domain.usecase

import com.dangjang.android.domain.model.GetMypageVO
import com.dangjang.android.domain.repository.MypageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MypageUseCase @Inject constructor(
    private val mypageRepository: MypageRepository
) {

    suspend fun getMypage(accessToken: String): Flow<GetMypageVO> =
        withContext(Dispatchers.IO) {
            mypageRepository.getMypage(accessToken)
        }
}