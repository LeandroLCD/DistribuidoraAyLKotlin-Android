package com.blipblipcode.distribuidoraayl.ui.utils

import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.isEmailValid(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.removeAccents():String{
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
}

fun Date.toFormattedString(): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return formatter.format(this)
}