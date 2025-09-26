package com.blipblipcode.distribuidoraayl.domain.throwable

class RequiredFieldException(message: String = "Required field", cause: Throwable? = null) : Throwable(message, cause) {
    override fun toString(): String {
        return "RequiredFieldException(message=${message ?: "No message"}, cause=${cause ?: "No cause"})"
    }
}