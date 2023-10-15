package com.dangjang.android

import android.app.Application
import android.content.Context
import com.dangjang.android.data.storage.InAppStorageHelperImpl
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.constants.VERSION_SPF_KEY
import com.dangjang.android.domain.constants.VERSION_TOKEN_KEY
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import com.dangjang.android.swm_logging.SWMLogging
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DangjangApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        NaverIdLoginSDK.initialize(this, BuildConfig.NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_SECRET, "dangjang")

        SWMLogging.init(
            //appVersion = BuildConfig.VERSION_NAME,
            //osNameAndVersion = "$ANDROID ${android.os.Build.VERSION.SDK_INT}",
            baseUrl = com.dangjang.android.data.BuildConfig.BASE_URL,
            serverPath = "api/log",
            token = getAccessToken() ?: ""
        )

        val sharedPreferences = applicationContext.getSharedPreferences(VERSION_SPF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(VERSION_TOKEN_KEY, BuildConfig.VERSION_NAME).apply()

        //TODO : 앱 꺼짐 방지 에러 핸들링 -> 홈 화면 이동
        Thread.setDefaultUncaughtExceptionHandler { t, e ->  }
    }

    companion object {
        private const val ANDROID = "Android"
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = applicationContext.getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)

        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }
}