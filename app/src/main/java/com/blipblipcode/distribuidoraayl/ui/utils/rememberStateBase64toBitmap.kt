package com.blipblipcode.distribuidoraayl.ui.utils

import android.graphics.BitmapFactory.decodeByteArray
import android.util.Base64.decode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun rememberStateBase64toBitmap(base64: String): ImageBitmap? {
    return remember(base64) {
        try {
            val imageBytes = decode(base64, android.util.Base64.DEFAULT)
            val bitmap = decodeByteArray(imageBytes, 0, imageBytes.size)
            bitmap?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
}