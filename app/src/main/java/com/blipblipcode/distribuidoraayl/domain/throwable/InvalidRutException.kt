package com.blipblipcode.distribuidoraayl.domain.throwable

class InvalidRutException (message: String? = "The rut is invalid", cause: Throwable? = null) : Throwable(message, cause) {

    override fun toString(): String {
        return "InvalidRutException(message=${message ?: "No message"}, cause=${cause ?: "No cause"})"
    }
}