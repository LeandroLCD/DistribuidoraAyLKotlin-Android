package com.blipblipcode.distribuidoraayl.domain.useCase.preferences

import com.blipblipcode.distribuidoraayl.domain.models.preferences.ECommerce
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.preferences.CredentialOf
import kotlinx.coroutines.flow.Flow

interface ISystemPreferencesRepository {

    suspend fun syncCredentialOf(): ResultType<CredentialOf>

    fun getCredentialOf(): Flow<CredentialOf>

    suspend fun syncECommerce(): ResultType<ECommerce>

    fun getECommerce(): Flow<ECommerce>

}