package com.dangjang.android.data.di

import com.dangjang.android.data.repository.IntroRepositoryImpl
import com.dangjang.android.data.repository.LoginRepositoryImpl
import com.dangjang.android.domain.repository.IntroRepository
import com.dangjang.android.domain.repository.LoginRepository
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
}