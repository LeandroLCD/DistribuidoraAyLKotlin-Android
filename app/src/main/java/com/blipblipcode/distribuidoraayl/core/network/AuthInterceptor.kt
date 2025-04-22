package com.blipblipcode.distribuidoraayl.core.network

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

    private var cachedApiKey: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        cachedApiKey?.let { apiKey ->
            request.addHeader("apikey", apiKey)
        } ?: run {
            runBlocking {
                systemPreferences.getCredentialOf().first().let { credentials ->
                    cachedApiKey = credentials.apikey
                    request.addHeader("apikey", credentials.apikey)
                }
            }
        }

        return chain.proceed(request.build())
    }
}
