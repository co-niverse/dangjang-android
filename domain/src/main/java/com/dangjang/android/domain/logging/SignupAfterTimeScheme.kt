package com.dangjang.android.domain.logging

import com.dangjang.android.domain.constants.APP_VERSION
import com.dangjang.android.domain.constants.Log_VERSION
import com.dangjang.android.swm_logging.SWMLogging.getSessionId
import com.dangjang.android.swm_logging.logging_scheme.ExposureScheme
import kotlin.properties.Delegates


class SignupAfterTimeScheme(
    stayTime: Double
) : ExposureScheme() {

    init {
        setLoggingScheme(
            eventLogName = "signup_after_time",
            screenName = "home",
            logVersion = Log_VERSION,
            appVersion = APP_VERSION,
            sessionId = getSessionId(),
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

        fun build(): SignupAfterTimeScheme {
            return SignupAfterTimeScheme(
                stayTime
            )
        }
    }
}