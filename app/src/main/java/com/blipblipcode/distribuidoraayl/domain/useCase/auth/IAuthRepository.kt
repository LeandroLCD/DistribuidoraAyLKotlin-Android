package com.blipblipcode.distribuidoraayl.domain.useCase.auth

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.User

interface IAuthRepository {

    fun getUser(): ResultType<User>

    suspend fun signInWithEmailAndPassword(email: String, password: String): ResultType<User>

    suspend fun createUserWithEmailAndPassword(email: String, password: String): ResultType<User>

    suspend fun sendPasswordResetEmail(email: String): ResultType<Unit>

    suspend fun sendEmailVerification(): ResultType<Unit>

    suspend fun reloadAccount(): ResultType<User>

    fun signOut()


}