package com.blipblipcode.distribuidoraayl.core.network

import android.util.Log
import com.blipblipcode.distribuidoraayl.domain.useCase.preferences.ISystemPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val systemPreferences: ISystemPreferencesRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        val credentials = systemPreferences.getCredentials()
        request.addHeader("apikey", credentials.apikey)

        Log.d("openFactura", "request: ${chain.request().url}")

        return chain.proceed(request.build())
    }
}
