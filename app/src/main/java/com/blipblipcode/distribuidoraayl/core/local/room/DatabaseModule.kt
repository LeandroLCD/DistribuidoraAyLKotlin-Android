package com.blipblipcode.distribuidoraayl.core.local.room

import android.content.Context
import androidx.room.Room
import com.blipblipcode.distribuidoraayl.core.local.room.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
     @Provides
     @Singleton
     fun provideDatabase(
         context: Context
     ): DataBaseApp {
         return Room.databaseBuilder(
             context,
             DataBaseApp::class.java,
             "distribuidoraayl.db"
         ).fallbackToDestructiveMigration(true)
             .build()
     }

        @Provides
        @Singleton
        fun provideProductDao(
            database: DataBaseApp
        ): ProductDao {
            return database.productDao()
        }
}