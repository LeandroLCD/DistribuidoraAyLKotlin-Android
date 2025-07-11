package com.blipblipcode.distribuidoraayl

import android.app.Application
import com.blipblipcode.library.DateTime
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StartApp:Application(){
    override fun onCreate() {
        super.onCreate()
        DateTime.init(applicationContext)
    }
}