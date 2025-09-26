package com.blipblipcode.distribuidoraayl.domain.throwable

class CustomerAlreadyExistsException (message: String? = "This Customer Already Exists", cause: Throwable? = null) : Throwable(message, cause) {

    override fun toString(): String {
        return "CustomerAlreadyExistsException(message=${message ?: "No message"}, cause=${cause ?: "No cause"})"
    }

}