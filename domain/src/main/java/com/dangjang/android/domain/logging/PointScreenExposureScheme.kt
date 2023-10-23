package com.dangjang.android.domain.logging

import com.dangjang.android.domain.constants.Log_VERSION
import com.dangjang.android.swm_logging.logging_scheme.ExposureScheme
import java.util.UUID


class PointScreenExposureScheme(
) : ExposureScheme() {

    init {
        setLoggingScheme(
            eventLogName = "exposure_log",
            screenName = "point",
            logVersion = Log_VERSION,
            appVersion = "1.0.2",
            sessionId = UUID.randomUUID().toString(),
            logData = mutableMapOf(
            )
        )
    }

    class Builder {
        fun build(): PointScreenExposureScheme {
            return PointScreenExposureScheme(
            )
        }
    }
}