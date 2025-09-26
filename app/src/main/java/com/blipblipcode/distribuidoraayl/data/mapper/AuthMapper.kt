package com.blipblipcode.distribuidoraayl.data.mapper

import com.blipblipcode.distribuidoraayl.domain.models.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUser(): User {
    return User(
        uid = uid,
        email = email!!,
        isEmailVerified = isEmailVerified
    )
}

fun AuthResult.toUser():User{
    return this.user!!.run {
        User(
            uid = uid,
            email = email!!,
            isEmailVerified = isEmailVerified
        )
    }
}