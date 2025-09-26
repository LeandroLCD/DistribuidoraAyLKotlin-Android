package com.blipblipcode.distribuidoraayl.domain.throwable

class UserDeletedException (message: String? = "User deleted", cause: Throwable? = null) : Throwable(message, cause) {
    override fun toString(): String {
        return "UserDeletedException(message=${message ?: "No message"}, cause=${cause ?: "No cause"})"
    }
}