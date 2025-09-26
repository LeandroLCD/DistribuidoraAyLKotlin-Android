package com.blipblipcode.distribuidoraayl.domain.useCase.customer


import com.blipblipcode.distribuidoraayl.domain.models.customer.Region
import kotlinx.coroutines.flow.Flow

interface IGetRegionsUseCase {
    operator fun invoke(): Flow<List<Region>>
}