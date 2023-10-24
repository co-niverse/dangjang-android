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
    clicked: Boolean
) : ClickScheme() {

    init {
        setLoggingScheme(
            eventLogName = "home_calendar_click",
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
//        private lateinit var age: String
//        fun setTitleName(titleName: String): Builder {
//            this.titleName = titleName
//            return this
//        }

//        fun setAge(age: String): Builder {
//            this.age = age
//            return this
//        }

        fun setClicked(clicked: Boolean): Builder {
            this.clicked = clicked
            return this
        }

        fun build(): CalendarClickScheme {
            return CalendarClickScheme(
                clicked
            )
        }
    }
}