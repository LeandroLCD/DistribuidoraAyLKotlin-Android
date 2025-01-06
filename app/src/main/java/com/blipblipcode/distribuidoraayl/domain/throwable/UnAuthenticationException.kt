package com.blipblipcode.distribuidoraayl.domain.throwable

class UnAuthenticationException(message: String? = "Un Authentication", cause: Throwable? = null) : Throwable(message, cause) {
    override fun toString(): String {
        return "UnAuthenticationException(message=${message ?: "No message"}, cause=${cause ?: "No cause"})"
    }
}