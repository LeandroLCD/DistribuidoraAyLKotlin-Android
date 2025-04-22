package com.blipblipcode.distribuidoraayl.domain.useCase.preferences.impl

import com.blipblipcode.distribuidoraayl.domain.models.ECommerce
import com.blipblipcode.distribuidoraayl.domain.useCase.preferences.IGetEcommerceUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.preferences.ISystemPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetEcommerceUseCase @Inject constructor(
    private val systemPreferencesRepository: ISystemPreferencesRepository
) : IGetEcommerceUseCase {
    override fun invoke(): Flow<ECommerce> {
        return systemPreferencesRepository.getECommerce()
    }
}