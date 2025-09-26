package com.blipblipcode.distribuidoraayl.domain.throwable

class SignInException(message: String? = "Sign In Exception", cause: Throwable? = null) : Throwable(message, cause) {

    override fun toString(): String {
        return "SignInException(message=${message ?: "No message"}, cause=${cause ?: "No cause"})"
    }
}