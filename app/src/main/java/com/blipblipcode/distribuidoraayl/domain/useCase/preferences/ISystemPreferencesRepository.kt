package com.blipblipcode.distribuidoraayl.domain.useCase.preferences

import com.blipblipcode.distribuidoraayl.domain.models.preferences.ECommerce
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.preferences.CredentialOf
import kotlinx.coroutines.flow.Flow

interface ISystemPreferencesRepository {

    // Métodos síncronos para credenciales
    fun getCredentials(): CredentialOf

    // Métodos para actualización asíncrona
    suspend fun syncCredentialOf(): ResultType<CredentialOf>
    suspend fun syncECommerce(): ResultType<ECommerce>

    // Observables para cambios
    fun observeCredentialOf(): Flow<CredentialOf>
    fun observeECommerce(): Flow<ECommerce>

}