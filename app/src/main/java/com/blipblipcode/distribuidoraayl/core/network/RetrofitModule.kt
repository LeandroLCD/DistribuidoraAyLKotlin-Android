package com.blipblipcode.distribuidoraayl.core.network

import com.blipblipcode.distribuidoraayl.domain.useCase.preferences.ISystemPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Singleton
    @Provides
    fun providerRetrofit( apiClient: OkHttpClient,
                          preferences: ISystemPreferencesRepository
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl("https://api.haulmer.com/v2/dte/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(apiClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideOpenFacturaApi(retrofit: Retrofit): IOpenFacturaApi {
        return retrofit.create(IOpenFacturaApi::class.java)
    }

    @Provides
    fun okHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .callTimeout(30L, TimeUnit.SECONDS)
            .build()
    }
}