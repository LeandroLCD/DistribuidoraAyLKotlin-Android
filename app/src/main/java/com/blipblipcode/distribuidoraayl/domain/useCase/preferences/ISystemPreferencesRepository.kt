package com.blipblipcode.distribuidoraayl.domain.useCase.preferences

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.preferences.CredentialOf

interface ISystemPreferencesRepository {

    suspend fun syncCredentialOf(): ResultType<CredentialOf>

    fun getCredentialOf(): CredentialOf?

}