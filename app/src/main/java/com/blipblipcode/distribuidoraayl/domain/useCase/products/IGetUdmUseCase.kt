package com.blipblipcode.distribuidoraayl.domain.useCase.products

import com.blipblipcode.distribuidoraayl.domain.models.products.Udm
import kotlinx.coroutines.flow.Flow

interface IGetUdmUseCase {
    operator fun invoke(): Flow<List<Udm>>
}