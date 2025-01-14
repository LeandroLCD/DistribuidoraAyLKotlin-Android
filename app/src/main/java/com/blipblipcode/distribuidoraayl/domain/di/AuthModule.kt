package com.blipblipcode.distribuidoraayl.domain.di

import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IForgotPasswordUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IGetUserUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.ILoginUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IReloadAccountUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.ISignOutUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IVerifiedEmailUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.impl.ForgotPasswordUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.impl.GetUserUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.impl.LoginUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.impl.ReloadAccountUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.impl.SignOutUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.impl.VerifiedEmailUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class AuthModule {

    @Binds
    abstract fun bindLoginUseCase(impl: LoginUseCase): ILoginUseCase

    @Binds
    abstract fun bindForgotPasswordUseCase(impl: ForgotPasswordUseCase): IForgotPasswordUseCase

    @Binds
    abstract fun bindReloadAccountUseCase(impl: ReloadAccountUseCase): IReloadAccountUseCase

    @Binds
    abstract fun bindGetUser(impl: GetUserUseCase): IGetUserUseCase

    @Binds
    abstract fun bindVerifiedEmailUseCase(impl: VerifiedEmailUseCase): IVerifiedEmailUseCase

    @Binds
    abstract fun bindSignOutUseCase(impl: SignOutUseCase): ISignOutUseCase

}