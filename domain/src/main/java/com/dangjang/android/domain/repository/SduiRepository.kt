package com.dangjang.android.domain.repository

import com.dangjang.android.domain.sdui.SduiCommonVO
import com.dangjang.android.domain.sdui.SduiSignupVO
import kotlinx.coroutines.flow.Flow

interface SduiRepository {

    fun getSduiSignup(): Flow<SduiSignupVO>
}