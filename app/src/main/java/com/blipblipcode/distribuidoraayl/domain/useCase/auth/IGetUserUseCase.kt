package com.blipblipcode.distribuidoraayl.domain.useCase.auth

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.User

interface IGetUserUseCase {
    suspend operator fun invoke(): ResultType<User>
}