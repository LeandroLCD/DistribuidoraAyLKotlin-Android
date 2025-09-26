package com.blipblipcode.distribuidoraayl.core.di.wok

import android.content.Context
import androidx.startup.Initializer
import com.blipblipcode.distribuidoraayl.core.di.wok.initializer.DependencyGraphInitializer
import com.blipblipcode.distribuidoraayl.core.local.room.DataBaseApp
import javax.inject.Inject

class DataBaseInitializer : Initializer<DataBaseApp> {

    @Inject
    lateinit var dataBase: DataBaseApp
    override fun create(context: Context): DataBaseApp {
        InitializerEntryPoint.resolve(context).inject(this)
        return dataBase
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(DependencyGraphInitializer::class.java)
    }
}