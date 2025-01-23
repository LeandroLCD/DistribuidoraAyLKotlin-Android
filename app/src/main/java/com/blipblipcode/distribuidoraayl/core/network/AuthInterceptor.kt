package com.blipblipcode.distribuidoraayl.core.network

import com.blipblipcode.distribuidoraayl.domain.useCase.preferences.ISystemPreferencesRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val systemPreferences: ISystemPreferencesRepository): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val url = chain.request().url
        println("URL: $url")
        systemPreferences.getCredentialOf()?.let{
            request.addHeader("apikey", it.apikey)
        }
        return chain.proceed(request.build())

    }
}