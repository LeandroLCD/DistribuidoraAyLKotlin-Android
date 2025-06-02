package com.blipblipcode.distribuidoraayl.core.local.room

import android.content.Context
import androidx.room.Room
import com.blipblipcode.distribuidoraayl.core.local.room.dao.ProductDao
import com.blipblipcode.distribuidoraayl.core.local.room.dao.ReportSaleDao
import com.blipblipcode.distribuidoraayl.core.local.room.migrations.MigrationsFactory
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
        ).addMigrations(MigrationsFactory.MIGRATION_3_4).build()
    }

    @Provides
    @Singleton
    fun provideProductDao(
        database: DataBaseApp
    ): ProductDao {
        return database.productDao()
    }

    @Provides
    @Singleton
    fun provideReportSale(
        database: DataBaseApp
    ): ReportSaleDao {
        return database.reportSaleDao()

    }
}