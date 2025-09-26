package com.blipblipcode.distribuidoraayl.domain.throwable

class UserDisabledException (message: String? = "User Disabled by admin", cause: Throwable? = null) : Throwable(message, cause) {

    override fun toString(): String {
        return "UserDisabledException(message=${message ?: "No message"}, cause=${cause ?: "No cause"})"
    }
}