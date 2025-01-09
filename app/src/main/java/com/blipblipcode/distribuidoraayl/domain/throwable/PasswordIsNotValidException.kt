package com.blipblipcode.distribuidoraayl.domain.throwable

class PasswordIsNotValidException(message: String? = "Password Is Not Valid", cause: Throwable? = null) : Throwable(message, cause) {

    override fun toString(): String {
        return "PasswordIsNotValidException(message=${message ?: "No message"}, cause=${cause ?: "No cause"})"
    }
}