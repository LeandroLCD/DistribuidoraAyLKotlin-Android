package com.blipblipcode.distribuidoraayl.core.repository

import com.blipblipcode.distribuidoraayl.data.repositiry.auth.AuthRepository
import com.blipblipcode.distribuidoraayl.data.repositiry.customer.CustomerRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IAuthRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
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

    @Singleton
    @Provides
    fun provideCustomerRepository(customerRepository: CustomerRepository): ICustomerRepository {
        return customerRepository
    }


}