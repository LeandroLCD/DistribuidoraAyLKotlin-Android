package com.blipblipcode.distribuidoraayl.core.firebase

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RemoteConfigModule {

    @Provides
    @Singleton
    fun providesRemoteConfig(): FirebaseRemoteConfig {
        val config = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3//600
        }

        return Firebase.remoteConfig.apply {
            setConfigSettingsAsync(config)
        }

    }
}