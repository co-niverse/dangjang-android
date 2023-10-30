package com.dangjang.android.swm_logging

import com.google.gson.GsonBuilder
import com.dangjang.android.swm_logging.logging_scheme.SWMLoggingScheme
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID
import java.util.concurrent.TimeUnit


object SWMLogging {

    private const val AUTHORIZATION = "Authorization"

    //private lateinit var appVersion: String
    //private lateinit var OSNameAndVersion: String
    private lateinit var baseUrl: String
    private lateinit var serverPath: String
    private lateinit var sessionId: String
    private lateinit var loggingService: LoggingService
    private val observable = PublishSubject.create<SWMLoggingScheme>() // 발행
    private val observer = object : Observer<SWMLoggingScheme> { // 구독
        override fun onSubscribe(d: Disposable) {
            println("Rx: 구독 시작")
        }

        override fun onNext(value: SWMLoggingScheme) {
            println("Rx: 아이템 받음: ${value.eventLogName}")
            runBlocking {
                val result = async { shotLogging(value) }
                println("Rx: 로깅 결과: ${result.await()}")
            }
        }

        override fun onError(e: Throwable) {
            println("Rx: 에러 발생: ${e.message}")
        }

        override fun onComplete() {
            println("Rx: 스트림 완료")
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    private val loggingRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    fun logEvent(swmLoggingScheme: SWMLoggingScheme) {
        observable.onNext(swmLoggingScheme)
    }

    suspend fun shotLogging(swmLoggingScheme: SWMLoggingScheme): Response<BaseDTO> {
        checkInitialized()
        return loggingService.postLogging(serverPath, swmLoggingScheme)
    }

    private fun checkInitialized() {
        check(SWMLogging::baseUrl.isInitialized) {
            "The 'baseUrl' property must be initialized. Initialize it by calling the 'init' method on the SwmLogging instance within your Application class."
        }
    }

    fun init(
        //appVersion: String,
        //osNameAndVersion: String,
        baseUrl: String,
        serverPath: String,
        sessionId: String
    ) {
        //SWMLogging.appVersion = appVersion
        //OSNameAndVersion = osNameAndVersion
        SWMLogging.baseUrl = baseUrl
        SWMLogging.serverPath = serverPath
        SWMLogging.sessionId = sessionId
        setLoggingService()
        observable.throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(observer)
    }

    private fun setLoggingService() {
        loggingService = loggingRetrofit.create(LoggingService::class.java)
    }

//    fun getOsNameAndVersion(): String {
//        return OSNameAndVersion
//    }

    fun getSessionId(): String {
        return sessionId
    }
}
