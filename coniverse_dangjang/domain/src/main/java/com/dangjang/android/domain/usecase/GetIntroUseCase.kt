package com.dangjang.android.domain.usecase

import android.util.Log
import com.dangjang.android.domain.model.IntroVO
import com.dangjang.android.domain.repository.IntroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetIntroUseCase @Inject constructor(
    private val introRepository: IntroRepository
) {
    suspend operator fun invoke(): Flow<IntroVO> = channelFlow {
        introRepository.getIntroApi().collectLatest {
            IntroVO(it.minVersion,it.latestVersion)
            send(it)
        }
    }
}