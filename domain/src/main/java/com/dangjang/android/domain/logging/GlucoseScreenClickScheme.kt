package com.dangjang.android.domain.logging

import com.dangjang.android.domain.constants.APP_VERSION
import com.dangjang.android.domain.constants.Log_VERSION
import com.dangjang.android.domain.model.TodayGuidesVO
import com.dangjang.android.domain.model.UserLogVO
import com.dangjang.android.swm_logging.logging_scheme.ClickScheme
import java.util.UUID
import kotlin.properties.Delegates


class GlucoseScreenClickScheme(
    bloodSugars: List<TodayGuidesVO>,
    userLog: UserLogVO
) : ClickScheme() {

    init {
        setLoggingScheme(
            eventLogName = "home_bloodsugar_click",
            screenName = "home",
            logVersion = Log_VERSION,
            appVersion = APP_VERSION,
            sessionId = UUID.randomUUID().toString(),
            logData = mutableMapOf(
                "bloodSugars" to bloodSugars,
                "userLog" to userLog
            )
        )
    }

    class Builder {
        private lateinit var bloodSugars: List<TodayGuidesVO>
        private lateinit var userLog: UserLogVO

        fun setBloodSugars(bloodSugars: List<TodayGuidesVO>): Builder {
            this.bloodSugars = bloodSugars
            return this
        }

        fun setUserLog(userLog: UserLogVO): Builder {
            this.userLog = userLog
            return this
        }

        fun build(): GlucoseScreenClickScheme {
            return GlucoseScreenClickScheme(
                bloodSugars,
                userLog
            )
        }
    }
}