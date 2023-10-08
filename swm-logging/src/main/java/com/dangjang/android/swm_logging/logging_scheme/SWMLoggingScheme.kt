package com.dangjang.android.swm_logging.logging_scheme

abstract class SWMLoggingScheme {
    open lateinit var eventLogName: String
    open lateinit var screenName: String
    open var logVersion: Int = 0
    //private val osVersionAndName: String = SWMLogging.getOsNameAndVersion()
    open lateinit var sessionId: String
    private var logData: MutableMap<String, Any>? = mutableMapOf()
    fun setLoggingScheme(
        eventLogName: String,
        screenName: String,
        logVersion: Int,
        sessionId: String,
        logData: MutableMap<String, Any>?
    ) {
        this.eventLogName = eventLogName
        this.screenName = screenName
        this.logVersion = logVersion
        this.sessionId = sessionId
        this.logData = logData
    }

    fun getLogData(): MutableMap<String, Any>? {
        return logData
    }
}
