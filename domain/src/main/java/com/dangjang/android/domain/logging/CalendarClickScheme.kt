package com.dangjang.android.domain.logging

import com.dangjang.android.domain.constants.APP_VERSION
import com.dangjang.android.domain.constants.Log_VERSION
import com.dangjang.android.swm_logging.SWMLogging
import com.dangjang.android.swm_logging.logging_scheme.ClickScheme


class CalendarClickScheme(
) : ClickScheme() {

    init {
        setLoggingScheme(
            eventLogName = "home_calendar_click",
            screenName = "home",
            logVersion = Log_VERSION,
            appVersion = APP_VERSION,
            sessionId = SWMLogging.getSessionId(),
            logData = mutableMapOf(
            )
        )
    }

    class Builder {

        fun build(): CalendarClickScheme {
            return CalendarClickScheme(
            )
        }
    }
}