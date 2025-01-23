package com.blipblipcode.distribuidoraayl.data.repositiry.of

import android.content.Context
import com.blipblipcode.distribuidoraayl.core.network.IOpenFacturaApi
import com.blipblipcode.distribuidoraayl.data.repositiry.BaseRepository
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.of.Taxpayer
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IOpenFacturaRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class OpenFacturaRepository @Inject constructor(
    dispatcher: CoroutineDispatcher,
    context: Context,
    private val openFacturaApi: IOpenFacturaApi
):BaseRepository(dispatcher, context), IOpenFacturaRepository {
    override suspend fun getTaxpayer(rut: String): ResultType<Taxpayer> {
        return makeCallNetwork {
            openFacturaApi.getTaxpayer(rut).mapToDomain()
        }
    }
}