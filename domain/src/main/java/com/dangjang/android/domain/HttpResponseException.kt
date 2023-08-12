package com.dangjang.android.data.datasource

class HttpResponseException(
    val status: HttpResponseStatus,
    val httpCode: Int,
    val errorRequestUrl: String,
    message: String,
    cause: Throwable?
) : Exception(message, cause)