package com.blipblipcode.distribuidoraayl.ui.utils

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.createBitmap
import androidx.core.graphics.get
import androidx.core.graphics.scale
import androidx.core.graphics.set

object BitmapUtil {


    /**
     * Convierte el bitmap a monocromo (blanco y negro)
     * Corregido para manejar correctamente códigos PDF417 y QR con transparencia
     */
    fun convertToMonochrome(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val monochromeBitmap = createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = bitmap[x, y]

                // Extraer valores RGB y Alpha
                val red = Color.red(pixel)
                val green = Color.green(pixel)
                val blue = Color.blue(pixel)
                val alpha = Color.alpha(pixel)


                if (alpha < 128) {
                    // Píxel transparente = fondo blanco
                    monochromeBitmap[x, y] = Color.WHITE
                } else {
                    // Píxel no transparente - evaluar si es negro o blanco
                    // Calcular luminancia usando la fórmula estándar
                    val luminance = (0.299 * red + 0.587 * green + 0.114 * blue).toInt()

                    // Para códigos de barras, usar umbral más sensible
                    val threshold = 128
                    val bwPixel = if (luminance < threshold) Color.BLACK else Color.WHITE

                    monochromeBitmap[x, y] = bwPixel
                }
            }
        }

        return monochromeBitmap
    }

    /**
     * Convierte el bitmap monocromo a array de bytes para la impresora
     * Cada byte representa 8 píxeles horizontales
     */
    fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val width = bitmap.width
        val height = bitmap.height

        // Calcular el ancho en bytes (cada byte = 8 píxeles)
        val widthBytes = (width + 7) / 8
        val dataSize = widthBytes * height
        val data = ByteArray(dataSize)

        var dataIndex = 0

        for (y in 0 until height) {
            for (x in 0 until widthBytes) {
                var byteValue = 0

                // Procesar 8 píxeles por byte
                for (bit in 0 until 8) {
                    val pixelX = x * 8 + bit

                    if (pixelX < width) {
                        val pixel = bitmap[pixelX, y]

                        // Verificar si el pixel es negro
                        // Para QR codes, generalmente los píxeles negros tienen valores RGB bajos
                        val isBlack = when {
                            pixel == Color.BLACK -> true
                            pixel == Color.WHITE -> false
                            else -> {
                                // Calcular luminancia para píxeles con color
                                val red = Color.red(pixel)
                                val green = Color.green(pixel)
                                val blue = Color.blue(pixel)
                                val luminance = (0.299 * red + 0.587 * green + 0.114 * blue).toInt()
                                luminance < 128 // Umbral para determinar si es negro
                            }
                        }

                        if (isBlack) {
                            // Bit 1 = píxel negro, bit 0 = píxel blanco
                            byteValue = byteValue or (1 shl (7 - bit))
                        }
                    }
                    // Si pixelX >= width, el bit queda en 0 (blanco)
                }

                data[dataIndex++] = byteValue.toByte()
            }
        }

        return data
    }

    /**
     * Función específica para manejar imágenes con transparencia
     * Optimizada para códigos PDF417 y QR
     */
    fun convertToMonochromeWithTransparency(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val monochromeBitmap = createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = bitmap[x, y]
                val alpha = Color.alpha(pixel)

                if (alpha < 128) {
                    // Píxel transparente = fondo blanco
                    monochromeBitmap[x, y] = Color.WHITE
                } else {
                    // Píxel no transparente
                    val red = Color.red(pixel)
                    val green = Color.green(pixel)
                    val blue = Color.blue(pixel)

                    // Para códigos de barras, los píxeles no transparentes
                    // generalmente representan los datos (negro)
                    // Pero verificamos por si acaso hay píxeles blancos no transparentes

                    if (red == 255 && green == 255 && blue == 255) {
                        // Píxel blanco no transparente
                        monochromeBitmap[x, y] = Color.WHITE
                    } else {
                        // Cualquier otro color no transparente se considera negro
                        monochromeBitmap[x, y] = Color.BLACK
                    }
                }
            }
        }

        return monochromeBitmap
    }

    /**
     * Redimensiona el bitmap si excede el ancho máximo, manteniendo la proporción
     */
    fun resizeBitmapIfNeeded(bitmap: Bitmap, maxWidth: Int): Bitmap {
        return if (bitmap.width > maxWidth) {
            val aspectRatio = bitmap.height.toFloat() / bitmap.width.toFloat()
            val newHeight = (maxWidth * aspectRatio).toInt()
            bitmap.scale(maxWidth, newHeight)
        } else {
            bitmap
        }
    }

}