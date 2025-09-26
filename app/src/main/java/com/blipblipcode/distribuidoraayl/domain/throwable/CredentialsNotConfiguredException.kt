package com.blipblipcode.distribuidoraayl.domain.throwable

class CredentialsNotConfiguredException (message: String? = "The credentials are not configured", cause: Throwable? = null) : Throwable(message, cause) {

    override fun toString(): String {
        return "CredentialsNotConfiguredException(message=${message ?: "No message"}, cause=${cause ?: "No cause"})"
    }
}