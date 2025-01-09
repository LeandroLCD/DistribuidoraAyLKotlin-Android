package com.blipblipcode.distribuidoraayl.ui.auth.login.models

data class DataField<T>(
    val value:T,
    val isError:Boolean = false,
    val errorException: Throwable? = null
)