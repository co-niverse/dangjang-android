package com.dangjang.android.domain.logging

import com.dangjang.android.domain.model.GetHomeExerciseVO
import com.dangjang.android.domain.model.UserLogVO
import com.dangjang.android.swm_logging.logging_scheme.ClickScheme
import java.util.UUID
import kotlin.properties.Delegates


class ExerciseScreenClickScheme(
    clicked: Boolean,
    exercise: GetHomeExerciseVO,
    userLog: UserLogVO
) : ClickScheme() {

    init {
        setLoggingScheme(
            eventLogName = "home_exercise_click",
            screenName = "home",
            logVersion = 1,
            appVersion = "1.0.2",
            sessionId = UUID.randomUUID().toString(),
            logData = mutableMapOf(
                "clicked" to clicked,
                "exercise" to exercise,
                "userLog" to userLog
            )
        )
    }

    class Builder {
        private var clicked by Delegates.notNull<Boolean>()
        private lateinit var exercise: GetHomeExerciseVO
        private lateinit var userLog: UserLogVO

        fun setClicked(clicked: Boolean): Builder {
            this.clicked = clicked
            return this
        }

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
                clicked,
                exercise,
                userLog
            )
        }
    }
}