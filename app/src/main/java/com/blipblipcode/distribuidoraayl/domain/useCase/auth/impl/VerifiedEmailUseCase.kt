package com.blipblipcode.distribuidoraayl.domain.useCase.auth.impl

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IAuthRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IVerifiedEmailUseCase
import javax.inject.Inject

internal class VerifiedEmailUseCase @Inject constructor(private val repository: IAuthRepository):
    IVerifiedEmailUseCase {
    override suspend fun invoke(): ResultType<Unit> {
        return repository.sendEmailVerification()

    }
}