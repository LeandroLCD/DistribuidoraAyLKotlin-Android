package com.blipblipcode.distribuidoraayl.ui.utils

fun String.isEmailValid(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}