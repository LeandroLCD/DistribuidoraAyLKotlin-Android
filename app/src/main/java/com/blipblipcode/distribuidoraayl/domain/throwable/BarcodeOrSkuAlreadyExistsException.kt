package com.blipblipcode.distribuidoraayl.domain.throwable

class BarcodeOrSkuAlreadyExistsException (message: String = "Sku Already Exists", cause: Throwable? = null) : Throwable(message, cause) {
    override fun toString(): String {
        return "BarcodeOrSkuAlreadyExistsException(message=${message ?: "No message"}, cause=${cause ?: "No cause"})"
    }
}