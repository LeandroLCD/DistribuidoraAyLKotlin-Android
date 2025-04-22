package com.blipblipcode.distribuidoraayl.domain.useCase.preferences

import com.blipblipcode.distribuidoraayl.domain.models.preferences.ECommerce
import kotlinx.coroutines.flow.Flow

interface IGetEcommerceUseCase {
    operator fun invoke(): Flow<ECommerce>
}