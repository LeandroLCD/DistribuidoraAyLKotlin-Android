package com.blipblipcode.distribuidoraayl.domain.useCase.auth

import com.blipblipcode.distribuidoraayl.domain.models.ResultType

interface IForgotPasswordUseCase {
    suspend operator fun invoke(email: String): ResultType<Unit>
}