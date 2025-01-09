package com.blipblipcode.distribuidoraayl.domain.useCase.auth

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.User

interface ILoginUseCase {
    suspend operator fun invoke(email: String, password: String): ResultType<User>
}