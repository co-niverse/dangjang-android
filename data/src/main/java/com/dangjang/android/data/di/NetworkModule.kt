package com.dangjang.android.data.di

import android.content.Context
import com.dangjang.android.data.BuildConfig
import com.dangjang.android.data.datasource.ChartApiService
import com.dangjang.android.data.datasource.HomeApiService
import com.dangjang.android.data.datasource.SplashApiService
import com.dangjang.android.data.datasource.LoginApiService
import com.dangjang.android.data.datasource.MypageApiService
import com.dangjang.android.data.datasource.SignupApiService
import com.dangjang.android.data.datasource.TokenApiService
import com.dangjang.android.data.interceptor.NetworkInterceptor
import com.dangjang.android.data.interceptor.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideRequestHttpLoggingInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor, @ApplicationContext context: Context) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(TokenInterceptor(context))
            .addInterceptor(NetworkInterceptor())
            .retryOnConnectionFailure(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideIntroApiService(retrofit: Retrofit): SplashApiService {
        return retrofit.create(SplashApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginApiSource(retrofit: Retrofit): LoginApiService {
        return retrofit.create(LoginApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSignupApiService(retrofit: Retrofit): SignupApiService{
        return retrofit.create(SignupApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeApiService(retrofit: Retrofit): HomeApiService {
        return retrofit.create(HomeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideChartApiService(retrofit: Retrofit): ChartApiService {
        return retrofit.create(ChartApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMypageApiService(retrofit: Retrofit): MypageApiService {
        return retrofit.create(MypageApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenApiService(retrofit: Retrofit): TokenApiService {
        return retrofit.create(TokenApiService::class.java)
    }
}