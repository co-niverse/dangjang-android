package com.dangjang.android.data.di

import android.content.Context
import com.dangjang.android.data.storage.InAppStorageHelperImpl
import com.dangjang.android.domain.storage.InAppStorageHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InAppStorageModule {
    @Provides
    @Singleton
    fun providesSharedPreferenceHelper(@ApplicationContext context: Context): InAppStorageHelper {
        return InAppStorageHelperImpl(context)
    }
}