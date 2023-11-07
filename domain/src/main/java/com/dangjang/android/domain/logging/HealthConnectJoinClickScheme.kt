package com.dangjang.android.domain.logging

import com.dangjang.android.domain.constants.APP_VERSION
import com.dangjang.android.domain.constants.Log_VERSION
import com.dangjang.android.swm_logging.SWMLogging.getSessionId
import com.dangjang.android.swm_logging.logging_scheme.ClickScheme
import kotlin.properties.Delegates


class HealthConnectJoinClickScheme(
    clicked: Boolean
) : ClickScheme() {

    init {
        setLoggingScheme(
            eventLogName = "healthconnect_join_click",
            screenName = "healthconnect",
            logVersion = Log_VERSION,
            appVersion = APP_VERSION,
            sessionId = getSessionId(),
            logData = mutableMapOf(
                "clicked" to clicked
            )
        )
    }

    class Builder {
        private var clicked by Delegates.notNull<Boolean>()

        fun setClicked(clicked: Boolean): Builder {
            this.clicked = clicked
            return this
        }

        fun build(): HealthConnectJoinClickScheme {
            return HealthConnectJoinClickScheme(
                clicked
            )
        }
    }
}