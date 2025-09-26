package com.blipblipcode.distribuidoraayl.ui.auth.login.models

data class DataField<T>(
    val value: T,
    val isError: Boolean = false,
    val errorException: Throwable? = null
) {
    fun isValid(): Boolean {
        return !isError && when (value) {
            null -> false
            is CharSequence -> value.isNotEmpty()
            is Collection<*> -> value.isNotEmpty()
            is Map<*, *> -> value.isNotEmpty()
            is Array<*> -> value.isNotEmpty()
            is Number -> value.toLong() != 0L
            else -> true
        }
    }
}