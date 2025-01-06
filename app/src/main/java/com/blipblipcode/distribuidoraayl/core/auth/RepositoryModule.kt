package com.blipblipcode.distribuidoraayl.core.auth

import com.blipblipcode.distribuidoraayl.data.repositiry.auth.AuthRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideAuthRepository(authRepository: AuthRepository): IAuthRepository {
        return authRepository
    }

}