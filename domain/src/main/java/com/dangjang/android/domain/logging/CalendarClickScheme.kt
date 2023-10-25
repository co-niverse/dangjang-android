package com.dangjang.android.domain.logging

import android.content.Context
import com.dangjang.android.domain.constants.APP_VERSION
import com.dangjang.android.domain.constants.Log_VERSION
import com.dangjang.android.domain.constants.VERSION_SPF_KEY
import com.dangjang.android.domain.constants.VERSION_TOKEN_KEY
import com.dangjang.android.swm_logging.logging_scheme.ClickScheme
import java.util.UUID
import kotlin.properties.Delegates


class CalendarClickScheme(
) : ClickScheme() {

    init {
        setLoggingScheme(
            eventLogName = "home_calendar_click",
            screenName = "home",
            logVersion = Log_VERSION,
            appVersion = APP_VERSION,
            sessionId = UUID.randomUUID().toString(),
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