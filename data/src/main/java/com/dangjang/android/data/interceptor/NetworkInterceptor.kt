package com.dangjang.android.data.interceptor

import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.nio.charset.Charset
import java.util.Locale


class NetworkInterceptor(
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val body = response.body

        if (body != null && body.contentType() != null && body.contentType()!!.subtype != null && body.contentType()!!.subtype.lowercase(
                Locale.getDefault()
            ) == "json"
        ) {
            var errorMessage = ""
            var errorCode = 200 // Assume default OK
            try {
                val source = body.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()
                val charset = body.contentType()!!.charset(Charset.forName("UTF-8"))
                // Clone the existing buffer is they can only read once so we still want to pass the original one to the chain.
                val json = buffer.clone().readString(charset!!)
                val obj = JsonParser().parse(json)
                // Capture error code an message.
                if (obj is JsonObject && obj.has("errorCode")) {
                    errorCode = (obj as JsonObject)["errorCode"].asInt
                }
                if (obj is JsonObject && obj.has("message")) {
                    errorMessage = (obj as JsonObject)["message"].asString
                }
            } catch (e: Exception) {
                Log.e("error", "Error: " + e.message)
            }

            if (errorCode > 399) {
                throw IOException("$errorMessage")
            }
        }

        return response
    }

}