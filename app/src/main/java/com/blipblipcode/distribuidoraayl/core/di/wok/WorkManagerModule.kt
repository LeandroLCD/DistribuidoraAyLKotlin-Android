package com.blipblipcode.distribuidoraayl.core.di.wok

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object WorkManagerModule {

    @Singleton
    @Provides
    fun provideWorkManager(context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

}
