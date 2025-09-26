package com.blipblipcode.distribuidoraayl.domain.throwable

class FormatInvalidException (message: String = "Invalid format", cause: Throwable? = null) : Throwable(message, cause) {
    override fun toString(): String {
        return "FormatInvalidException(message=${message ?: "No message"}, cause=${cause ?: "No cause"})"
    }
}