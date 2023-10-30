package com.dangjang.android.domain.logging

import com.dangjang.android.domain.constants.APP_VERSION
import com.dangjang.android.domain.constants.Log_VERSION
import com.dangjang.android.domain.model.GetHomeExerciseVO
import com.dangjang.android.domain.model.UserLogVO
import com.dangjang.android.swm_logging.SWMLogging.getSessionId
import com.dangjang.android.swm_logging.logging_scheme.ClickScheme


class ExerciseScreenClickScheme(
    exercise: GetHomeExerciseVO,
    userLog: UserLogVO
) : ClickScheme() {

    init {
        setLoggingScheme(
            eventLogName = "home_exercise_click",
            screenName = "home",
            logVersion = Log_VERSION,
            appVersion = APP_VERSION,
            sessionId = getSessionId(),
            logData = mutableMapOf(
                "exercise" to exercise,
                "userLog" to userLog
            )
        )
    }

    class Builder {
        private lateinit var exercise: GetHomeExerciseVO
        private lateinit var userLog: UserLogVO

        fun setExercise(exercise: GetHomeExerciseVO): Builder {
            this.exercise = exercise
            return this
        }

        fun setUserLog(userLog: UserLogVO): Builder {
            this.userLog = userLog
            return this
        }

        fun build(): ExerciseScreenClickScheme {
            return ExerciseScreenClickScheme(
                exercise,
                userLog
            )
        }
    }
}