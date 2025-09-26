package com.blipblipcode.distribuidoraayl.domain.throwable

class EmailIsNotValidException (message: String? = "Email Is Not Valid", cause: Throwable? = null) : Throwable(message, cause) {

    override fun toString(): String {
        return "EmailIsNotValidException(message=${message ?: "No message"}, cause=${cause ?: "No cause"})"
    }
}