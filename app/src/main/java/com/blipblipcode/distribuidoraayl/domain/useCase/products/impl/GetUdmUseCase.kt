package com.blipblipcode.distribuidoraayl.domain.useCase.products.impl

import com.blipblipcode.distribuidoraayl.domain.models.products.Udm
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetUdmUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetUdmUseCase @Inject constructor(private val repository: IProductsRepository):
    IGetUdmUseCase {
    override fun invoke(): Flow<List<Udm>> {
        return repository.getUds()
    }
}