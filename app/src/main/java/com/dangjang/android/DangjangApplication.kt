package com.dangjang.android

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DangjangApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, com.dangjang.android.BuildConfig.KAKAO_APP_KEY)
    }
}