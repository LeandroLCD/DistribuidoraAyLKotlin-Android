package com.blipblipcode.distribuidoraayl.core.system

import android.content.Context
import androidx.annotation.Keep
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Keep
@Module
@InstallIn(SingletonComponent::class)
object DataStorePreferences {
    @Singleton
    @Provides
    fun getDataStore(context: Context): DataStore<Preferences> {
        return context.dataStore(name = "preferences")
    }

    private fun Context.dataStore(name:String) : DataStore<Preferences> {
        return preferencesDataStore(name).getValue(this, DataStore<Preferences>::javaClass)
    }

}