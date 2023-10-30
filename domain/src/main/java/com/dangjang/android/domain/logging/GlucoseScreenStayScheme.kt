package com.dangjang.android.domain.logging

import com.dangjang.android.domain.constants.APP_VERSION
import com.dangjang.android.domain.constants.Log_VERSION
import com.dangjang.android.swm_logging.SWMLogging.getSessionId
import com.dangjang.android.swm_logging.logging_scheme.ExposureScheme
import kotlin.properties.Delegates


class GlucoseScreenStayScheme(
    stayTime: Double
) : ExposureScheme() {

    init {
        setLoggingScheme(
            eventLogName = "bloodsugar_stay_time",
            screenName = "bloodsugar",
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
//        private lateinit var age: String
//        fun setTitleName(titleName: String): Builder {
//            this.titleName = titleName
//            return this
//        }

//        fun setAge(age: String): Builder {
//            this.age = age
//            return this
//        }

        fun setStayTime(stayTime: Double): Builder {
            this.stayTime = stayTime
            return this
        }

        fun build(): GlucoseScreenStayScheme {
            return GlucoseScreenStayScheme(
                stayTime
            )
        }
    }
}