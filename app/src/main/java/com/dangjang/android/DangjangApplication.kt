package com.dangjang.android

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.v2.user.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DangjangApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "APP_KEY")
    }
}