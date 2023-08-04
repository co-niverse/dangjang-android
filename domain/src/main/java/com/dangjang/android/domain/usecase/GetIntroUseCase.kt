package com.dangjang.android.domain.usecase

import com.dangjang.android.domain.model.IntroVO
import com.dangjang.android.domain.repository.IntroRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetIntroUseCase @Inject constructor(
    private val introRepository: IntroRepository
) {
    suspend fun getIntro(): Flow<IntroVO> =
        withContext(Dispatchers.IO) {
            introRepository.getIntroApi()
        }
}