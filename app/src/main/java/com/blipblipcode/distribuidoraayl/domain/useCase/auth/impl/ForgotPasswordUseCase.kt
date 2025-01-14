package com.blipblipcode.distribuidoraayl.domain.useCase.auth.impl

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IAuthRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IForgotPasswordUseCase
import javax.inject.Inject

internal class ForgotPasswordUseCase @Inject constructor( private val repository: IAuthRepository) : IForgotPasswordUseCase {
    override suspend fun invoke(email: String): ResultType<Unit> {
        return repository.sendPasswordResetEmail(email)
    }
}