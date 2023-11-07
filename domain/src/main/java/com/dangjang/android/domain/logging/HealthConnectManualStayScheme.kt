package com.dangjang.android.domain.logging

import com.dangjang.android.domain.constants.APP_VERSION
import com.dangjang.android.domain.constants.Log_VERSION
import com.dangjang.android.swm_logging.SWMLogging.getSessionId
import com.dangjang.android.swm_logging.logging_scheme.ExposureScheme
import kotlin.properties.Delegates


class HealthConnectManualStayScheme(
    scrolled: Boolean,
    stayTime: Double,
    joined: Boolean
) : ExposureScheme() {

    init {
        setLoggingScheme(
            eventLogName = "healthconnect_manual",
            screenName = "healthconnect",
            logVersion = Log_VERSION,
            appVersion = APP_VERSION,
            sessionId = getSessionId(),
            logData = mutableMapOf(
                "scrolled" to scrolled,
                "stayTime" to stayTime,
                "joined" to joined
            )
        )
    }

    class Builder {
        private var scrolled by Delegates.notNull<Boolean>()
        private var stayTime by Delegates.notNull<Double>()
        private var joined by Delegates.notNull<Boolean>()

        fun setScrolled(scrolled: Boolean): Builder {
            this.scrolled = scrolled
            return this
        }

        fun setStayTime(stayTime: Double): Builder {
            this.stayTime = stayTime
            return this
        }

        fun setJoined(joined: Boolean): Builder {
            this.joined = joined
            return this
        }

        fun build(): HealthConnectManualStayScheme {
            return HealthConnectManualStayScheme(
                scrolled,
                stayTime,
                joined
            )
        }
    }
}