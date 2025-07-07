package com.blipblipcode.distribuidoraayl.data.repositiry.files

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.core.graphics.createBitmap
import androidx.core.graphics.get
import androidx.core.graphics.scale
import androidx.core.graphics.set
import com.blipblipcode.distribuidoraayl.domain.models.printState.PrinterState
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocumentElectronic
import com.blipblipcode.distribuidoraayl.domain.models.sales.DteType
import com.blipblipcode.distribuidoraayl.domain.models.sales.Payment
import com.blipblipcode.distribuidoraayl.domain.useCase.printer.IPrinterRepository
import com.blipblipcode.distribuidoraayl.ui.utils.toBitmap
import com.dantsu.escposprinter.EscPosCharsetEncoding
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@Suppress("DEPRECATION")
class PrinterRepository @Inject constructor(
    context: Context
) : IPrinterRepository {
    private val _printerState = MutableSharedFlow<PrinterState>(replay = 1, extraBufferCapacity = 1)

    override val printerState = _printerState.asSharedFlow()
    private lateinit var printer: EscPosPrinter

    private lateinit var connection: BluetoothConnection
    private val _broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothDevice.ACTION_ACL_CONNECTED -> {
                    val device =
                        intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    if (device != null) {
                        Log.d("Bluetooth", "Connected is connect")
                        _printerState.tryEmit(PrinterState.Connected)
                    }
                }

                BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                    Log.d("Bluetooth", "Connected is disconect")
                    _printerState.tryEmit(PrinterState.Disconnected)
                }

                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    when (intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR
                    )) {
                        BluetoothAdapter.STATE_OFF -> {
                            Log.d("Bluetooth", "STATE_OFF")
                            _printerState.tryEmit(PrinterState.BluetoothDisabled)
                        }

                        BluetoothAdapter.STATE_ON -> {
                            Log.d("Bluetooth", "STATE_ON")
                            _printerState.tryEmit(PrinterState.Idle)
                        }
                    }
                }
            }
        }
    }

    init {
        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        }

        context.registerReceiver(_broadcastReceiver, filter)
    }

    override fun connect(): Boolean {
        return try {
            if (!::connection.isInitialized || !connection.isConnected) {
                connection = BluetoothPrintersConnections.selectFirstPaired()!!
                printer = EscPosPrinter(
                    connection,
                    203,
                    48f,
                    32,
                    EscPosCharsetEncoding("windows-1252", 16)
                )
            }
            _printerState.tryEmit(PrinterState.Connected)
        } catch (e: Exception) {
            _printerState.tryEmit(PrinterState.Exception(e))
            false
        }
    }

    override fun disconnect() {
        printer.disconnectPrinter()
    }

    override fun print(document: DocumentElectronic) {

        _printerState.tryEmit(PrinterState.Printing)

        val printText = StringBuilder()

        // Encabezado con datos del documento
        val docName = when (document.docType) {
              DteType.ORDER_NOTE -> "NOTA DE PEDIDO"
              DteType.INVOICE -> "FACTURA"
              DteType.CREDIT_NOTE -> "NOTA DE CREDITO"
          }
          printText.append("[C]<b>RUT: ${document.sale.eCommerce.rut}</b>\n")
          printText.append("[C]<b>$docName</b>\n")
          printText.append("[C]<b>N- ${document.number}</b>\n")
          printText.append("[C]\n")
          printText.append("[C]\n")

          // Datos del emisor
          printText.append("[L]${document.sale.eCommerce.companyName}\n")
          printText.append("[L]${document.sale.eCommerce.businessLine}\n")
          printText.append("[L]${document.sale.eCommerce.addressOrigin}\n")
          printText.append("[L]${document.sale.eCommerce.phone}\n")
          printText.append("[C]________________________________\n")

          // Datos del receptor
          printText.append("[L]FECHA: ${document.sale.date.format("yyyy-MM-dd")}\n")
          printText.append("[L]RUT: ${document.sale.receiver.rut}\n")
          printText.append("[L]SR(ES): ${document.sale.receiver.name}\n")
          printText.append("[L]GIRO: ${document.sale.receiver.turn ?: "Sin giro"}\n")
          printText.append("[L]DIR.: ${document.sale.receiver.address}\n")
          printText.append("[C]________________________________\n")

          // Forma de pago
          printText.append("[L]FORMA DE PAGO: ${if (document.payment == Payment.Cash) "CONTADO" else "CREDITO"}\n")
          printText.append("[C]________________________________\n\n")

          // Encabezado items
          printText.append("[L]<b>ARTICULO[R]VALOR</b>\n")
          printText.append("[C]________________________________\n")
          // Items
          document.sale.items.forEach { item ->
              printText.append("[L]${item.sku}-${item.name}\n")
              printText.append("[L]${item.quantity} X ${item.price}[R]${item.price * item.quantity}\n")
          }

          printText.append("[C]________________________________\n")

          // Totales
          printText.append("[L]Monto Neto:[R]${document.sale.totals.netAmount}\n")
          printText.append("[L]IVA ${document.sale.totals.tax}%:[R]${document.sale.totals.taxAmount}\n")
          printText.append("[L]Monto Total:[R]${document.sale.totals.total}\n")

          printText.append("[C]________________________________\n")


        // Si es factura y tiene timbre fiscal document.docType != DteType.ORDER_NOTE &&
        if (!document.timbre.isNullOrEmpty()) {
            val bitmap = document.timbre.toBitmap()!!
            val hasTransparency = bitmap.hasAlpha()

            // Procesar la imagen
            val processedBitmap = BitmapUtil.resizeBitmapIfNeeded(bitmap, 384)
            val monochromeBitmap = if (hasTransparency) {
                BitmapUtil.convertToMonochromeWithTransparency(processedBitmap)
            } else {
                BitmapUtil.convertToMonochrome(processedBitmap)
            }
            val timbreHex =
                PrinterTextParserImg.bitmapToHexadecimalString(printer, monochromeBitmap, false)


            timbreHex?.let {
                printText.append("[C]<img>$timbreHex</img>\n")
                printText.append("[C]Timbre Electrónico S.I.I\n")
                printText.append("[C]Res. ${document.resolution}\n")
                printText.append("[C]Verifique en www.sii.cl\n")
            }

        } else {
            printText.append("[C]SIN VALIDEZ TRIBUTARIA\n")
        }
        try {
            printer.printFormattedText(printText.toString())
            /*document.timbre?.let {base64String->
                val cmdImg  = generatePDF417Command(base64String, maxWidth = 384, mode = 0)

                connection.write(cmdImg)
                connection.send()
            }*/
            _printerState.tryEmit(PrinterState.Ready)

        } catch (e: Exception) {
            _printerState.tryEmit(PrinterState.Exception(e))
        }
    }


    /**
     */
    fun generatePDF417Command(
        base64String: String,
        maxWidth: Int = 384,
        mode: Int = 0
    ): ByteArray? {
        val bitmap = base64String.toBitmap() ?: return null

        // Verificar si la imagen tiene transparencia
        val hasTransparency = bitmap.hasAlpha()

        // Procesar la imagen
        val processedBitmap = BitmapUtil.resizeBitmapIfNeeded(bitmap, maxWidth)
        val monochromeBitmap = if (hasTransparency) {
            BitmapUtil.convertToMonochromeWithTransparency(processedBitmap)
        } else {
            BitmapUtil.convertToMonochrome(processedBitmap)
        }

        val imageData = BitmapUtil.convertBitmapToByteArray(monochromeBitmap)

        return generateRasterBitmapCommand(
            width = monochromeBitmap.width,
            height = monochromeBitmap.height,
            mode = mode,
            imageData = imageData
        )
    }


    private fun generateRasterBitmapCommand(
        width: Int,
        height: Int,
        mode: Int = 0, // 0=Normal, 1=Double-width, 2=Double-height, 3=Quadruple
        imageData: ByteArray
    ): ByteArray {
        // Calcular xL, xH (ancho en bytes)
        val widthBytes = (width + 7) / 8 // Redondear hacia arriba
        val xL = widthBytes and 0xFF
        val xH = (widthBytes shr 8) and 0xFF

        // Calcular yL, yH (alto en líneas)
        val yL = height and 0xFF
        val yH = (height shr 8) and 0xFF

        // Crear comando: GS v 0 m xL xH yL yH d1...dk
        val commandSize = 8 + imageData.size
        val command = ByteArray(commandSize)

        // Cabecera del comando GS v 0
        command[0] = 0x1D.toByte() // GS
        command[1] = 0x76.toByte() // v
        command[2] = 0x30.toByte() // 0
        command[3] = mode.toByte() // m (modo)
        command[4] = xL.toByte()   // xL
        command[5] = xH.toByte()   // xH
        command[6] = yL.toByte()   // yL
        command[7] = yH.toByte()   // yH

        // Copiar datos de imagen
        System.arraycopy(imageData, 0, command, 8, imageData.size)

        return command
    }



}

object BitmapUtil {
    /**
     * Función de debug específica para analizar transparencia
     */
    fun debugBitmapInfo(bitmap: Bitmap): String {
        val width = bitmap.width
        val height = bitmap.height

        var transparentPixels = 0
        var opaqueBlackPixels = 0
        var opaqueWhitePixels = 0
        var opaqueOtherPixels = 0

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = bitmap[x, y]
                val alpha = Color.alpha(pixel)

                if (alpha < 128) {
                    transparentPixels++
                } else {
                    val red = Color.red(pixel)
                    val green = Color.green(pixel)
                    val blue = Color.blue(pixel)

                    when {
                        red == 0 && green == 0 && blue == 0 -> opaqueBlackPixels++
                        red == 255 && green == 255 && blue == 255 -> opaqueWhitePixels++
                        else -> opaqueOtherPixels++
                    }
                }
            }
        }

        val totalPixels = width * height

        return """
        Análisis de Transparencia:
        - Dimensiones: ${width}x${height}
        - Total píxeles: $totalPixels
        - Píxeles transparentes: $transparentPixels (${(transparentPixels * 100f / totalPixels).toInt()}%)
        - Píxeles opacos negros: $opaqueBlackPixels (${(opaqueBlackPixels * 100f / totalPixels).toInt()}%)
        - Píxeles opacos blancos: $opaqueWhitePixels (${(opaqueWhitePixels * 100f / totalPixels).toInt()}%)
        - Píxeles opacos otros colores: $opaqueOtherPixels (${(opaqueOtherPixels * 100f / totalPixels).toInt()}%)
        - Config: ${bitmap.config}
        - Has Alpha: ${bitmap.hasAlpha()}
    """.trimIndent()
    }

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

                // Para códigos PDF417/QR con transparencia:
                // - Píxeles transparentes (alpha < 128) = fondo blanco
                // - Píxeles no transparentes con color oscuro = datos negros
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

