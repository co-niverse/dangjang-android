package com.dangjang.android.domain.logging

import com.dangjang.android.domain.constants.APP_VERSION
import com.dangjang.android.domain.constants.Log_VERSION
import com.dangjang.android.swm_logging.logging_scheme.ExposureScheme
import java.util.UUID
import kotlin.properties.Delegates


class MypageScreenExposureScheme(
    stayTime: Double
) : ExposureScheme() {

    init {
        setLoggingScheme(
            eventLogName = "exposure_log",
            screenName = "mypage",
            logVersion = Log_VERSION,
            appVersion = APP_VERSION,
            sessionId = UUID.randomUUID().toString(),
            logData = mutableMapOf(
                "stayTime" to stayTime
            )
        )
    }

    class Builder {
        private var stayTime by Delegates.notNull<Double>()

        fun setStayTime(stayTime: Double): Builder {
            this.stayTime = stayTime
            return this
        }
        fun build(): MypageScreenExposureScheme {
            return MypageScreenExposureScheme(
                stayTime
            )
        }
    }
}