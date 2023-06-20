package com.dangjang.android.domain.usecase

import com.dangjang.android.domain.repository.IntroRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetIntroUseCase @Inject constructor(
    private val introRepository: IntroRepository
) {
    operator fun invoke() = flow {
        introRepository.getIntroApi(1).collectLatest {
            emit(it)
        }
    }
}