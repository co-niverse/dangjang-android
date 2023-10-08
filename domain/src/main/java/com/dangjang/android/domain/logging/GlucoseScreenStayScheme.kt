package com.dangjang.android.domain.logging

import com.dangjang.android.swm_logging.logging_scheme.ExposureScheme
import java.util.UUID
import kotlin.properties.Delegates


class GlucoseScreenStayScheme(
    stayTime: Double
) : ExposureScheme() {

    init {
        setLoggingScheme(
            eventLogName = "bloodsugar_stay_time",
            screenName = "bloodsugar",
            logVersion = 1,
            sessionId = UUID.randomUUID().toString(),
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