package com.blipblipcode.distribuidoraayl.domain.useCase.auth.impl

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.User
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IAuthRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IReloadAccountUseCase
import javax.inject.Inject

internal class ReloadAccountUseCase @Inject constructor(private val repository: IAuthRepository): IReloadAccountUseCase {
    override suspend fun invoke(): ResultType<User> {
        return repository.reloadAccount()
    }

}