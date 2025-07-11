package com.blipblipcode.distribuidoraayl.domain.di

import com.blipblipcode.distribuidoraayl.domain.useCase.preferences.IGetEcommerceUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.preferences.impl.GetEcommerceUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class PreferencesModule {

    @Binds
    abstract fun bindEcommercePreferences(useCase: GetEcommerceUseCase): IGetEcommerceUseCase

}