package com.blipblipcode.distribuidoraayl.domain.useCase.auth.impl

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.User
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IAuthRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IGetUserUseCase
import javax.inject.Inject

internal class GetUserUseCase @Inject constructor(private val repository: IAuthRepository) :
    IGetUserUseCase {
    override suspend fun invoke(): ResultType<User> {
        return repository.getUser()
    }

}