package com.blipblipcode.distribuidoraayl.domain.throwable

class BackendErrorException (message: String, cause: Throwable? = null) : Throwable(message, cause) {

    override fun toString(): String {
        return "BackendErrorException(message=${message ?: "No message"}, cause=${cause ?: "No cause"})"
    }
}