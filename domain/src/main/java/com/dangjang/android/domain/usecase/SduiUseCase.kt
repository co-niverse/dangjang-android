package com.dangjang.android.domain.usecase

import com.dangjang.android.domain.repository.SduiRepository
import com.dangjang.android.domain.sdui.SduiCommonVO
import com.dangjang.android.domain.sdui.SduiSignupVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SduiUseCase @Inject constructor(
    private val sduiRepository: SduiRepository
) {

    suspend fun getSduiSignup(): Flow<SduiSignupVO> =
        withContext(Dispatchers.IO) {
            sduiRepository.getSduiSignup()
        }
}