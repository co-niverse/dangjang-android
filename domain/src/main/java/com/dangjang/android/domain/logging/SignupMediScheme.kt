package com.dangjang.android.domain.logging

import com.dangjang.android.domain.constants.Log_VERSION
import com.dangjang.android.swm_logging.logging_scheme.ExposureScheme
import java.util.UUID
import kotlin.properties.Delegates


class SignupMediScheme(
    stayTime: Double
) : ExposureScheme() {

    init {
        setLoggingScheme(
            eventLogName = "signup_stay_time",
            screenName = "signupMedi",
            logVersion = Log_VERSION,
            appVersion = "1.0.2",
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

        fun build(): SignupMediScheme {
            return SignupMediScheme(
                stayTime
            )
        }
    }
}