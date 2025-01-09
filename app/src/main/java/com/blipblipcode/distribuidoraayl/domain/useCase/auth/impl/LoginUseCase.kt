package com.blipblipcode.distribuidoraayl.domain.useCase.auth.impl

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.User
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IAuthRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.ILoginUseCase
import javax.inject.Inject

internal class LoginUseCase @Inject constructor(private val repository: IAuthRepository):
    ILoginUseCase {
    override suspend fun invoke(email: String, password: String): ResultType<User> {
        return repository.signInWithEmailAndPassword(email, password)
    }
}