package com.blipblipcode.distribuidoraayl.domain.useCase.auth.impl

import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IAuthRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.ISignOutUseCase
import javax.inject.Inject

internal class SignOutUseCase @Inject constructor(private val repository: IAuthRepository):
    ISignOutUseCase {
    override fun invoke() {
        repository.signOut()
    }

}