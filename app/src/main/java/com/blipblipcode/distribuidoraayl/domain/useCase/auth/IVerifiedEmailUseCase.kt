package com.blipblipcode.distribuidoraayl.domain.useCase.auth

import com.blipblipcode.distribuidoraayl.domain.models.ResultType

interface IVerifiedEmailUseCase {
    suspend operator fun invoke(): ResultType<Unit>
}