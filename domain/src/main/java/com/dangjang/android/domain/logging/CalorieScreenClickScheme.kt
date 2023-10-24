package com.dangjang.android.domain.logging

import com.dangjang.android.domain.constants.APP_VERSION
import com.dangjang.android.domain.constants.Log_VERSION
import com.dangjang.android.domain.model.UserLogVO
import com.dangjang.android.swm_logging.logging_scheme.ClickScheme
import java.util.UUID
import kotlin.properties.Delegates


class CalorieScreenClickScheme(
    clicked: Boolean,
    userLog: UserLogVO
) : ClickScheme() {

    init {
        setLoggingScheme(
            eventLogName = "home_calorie_click",
            screenName = "home",
            logVersion = Log_VERSION,
            appVersion = APP_VERSION,
            sessionId = UUID.randomUUID().toString(),
            logData = mutableMapOf(
                "clicked" to clicked
            )
        )
    }

    class Builder {
        private var clicked by Delegates.notNull<Boolean>()
        private lateinit var userLog: UserLogVO

        fun setClicked(clicked: Boolean): Builder {
            this.clicked = clicked
            return this
        }

        fun setUserLog(userLog: UserLogVO): Builder {
            this.userLog = userLog
            return this
        }

        fun build(): CalorieScreenClickScheme {
            return CalorieScreenClickScheme(
                clicked,
                userLog
            )
        }
    }
}