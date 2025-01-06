package com.blipblipcode.distribuidoraayl.domain.throwable

class NetworkException(message: String? = "Internet connection not available", cause: Throwable? = null):Throwable(message, cause) {
    override fun toString(): String {
        return "NetworkException(message=${message ?: "No message"}, cause=${cause ?: "No cause"})"
    }
}