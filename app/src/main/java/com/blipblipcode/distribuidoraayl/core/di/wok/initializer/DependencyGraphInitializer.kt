package com.blipblipcode.distribuidoraayl.core.di.wok.initializer

import android.content.Context
import androidx.startup.Initializer
import com.blipblipcode.distribuidoraayl.core.di.wok.InitializerEntryPoint

class DependencyGraphInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        InitializerEntryPoint.resolve(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}