package com.blipblipcode.distribuidoraayl.domain.di

import com.blipblipcode.distribuidoraayl.domain.useCase.auth.ILoginUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.impl.LoginUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class AuthModule {

    @Binds
    abstract fun bindLoginUseCase(impl: LoginUseCase): ILoginUseCase

}