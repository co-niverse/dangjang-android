package com.dangjang.android.data.di

import com.dangjang.android.data.repository.ChartRepositoryImpl
import com.dangjang.android.data.repository.HomeRepositoryImpl
import com.dangjang.android.data.repository.IntroRepositoryImpl
import com.dangjang.android.data.repository.LoginRepositoryImpl
import com.dangjang.android.data.repository.SignupRepositoryImpl
import com.dangjang.android.domain.repository.ChartRepository
import com.dangjang.android.domain.repository.HomeRepository
import com.dangjang.android.domain.repository.IntroRepository
import com.dangjang.android.domain.repository.LoginRepository
import com.dangjang.android.domain.repository.SignupRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsIntroRepository(
        introRepositoryImpl: IntroRepositoryImpl
    ) : IntroRepository

    @Binds
    fun bindsLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl
    ) : LoginRepository

    @Binds
    fun bindsSignupRepository(
        signupRepositoryImpl: SignupRepositoryImpl
    ) : SignupRepository

    @Binds
    fun bindsHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ) : HomeRepository

    @Binds
    fun bindsChartRepository(
        chartRepositoryImpl: ChartRepositoryImpl
    ) : ChartRepository

}