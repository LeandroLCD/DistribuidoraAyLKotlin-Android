package com.blipblipcode.distribuidoraayl.ui.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeByteArray
import android.util.Base64.decode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun rememberStateBase64toBitmap(base64: String): ImageBitmap? {
    return remember(base64) {
        base64.toBitmap()?.asImageBitmap()
    }
}

fun String.toBitmap(): Bitmap? {
   return try {
        val imageBytes = decode(this, android.util.Base64.DEFAULT)
        decodeByteArray(imageBytes, 0, imageBytes.size)
    } catch (_: Exception) {
        null
    }
}