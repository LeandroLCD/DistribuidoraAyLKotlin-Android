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
import com.blipblipcode.distribuidoraayl.data.repositiry.BaseRepository
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.printState.PrinterState
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocumentElectronic
import com.blipblipcode.distribuidoraayl.domain.models.sales.DteType
import com.blipblipcode.distribuidoraayl.domain.models.sales.Payment
import com.blipblipcode.distribuidoraayl.domain.useCase.printer.IPrinterRepository
import com.blipblipcode.distribuidoraayl.ui.utils.BitmapUtil
import com.blipblipcode.distribuidoraayl.ui.utils.toBitmap
import com.dantsu.escposprinter.EscPosCharsetEncoding
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.Dispatcher
import javax.inject.Inject

@Suppress("DEPRECATION")
class PrinterRepository @Inject constructor(
    dispatcher: CoroutineDispatcher,
    context: Context
) : IPrinterRepository, BaseRepository(dispatcher, context) {
    private val _printerState = MutableSharedFlow<PrinterState>(replay = 1, extraBufferCapacity = 1)

    override val printerState = _printerState.asSharedFlow()
    private lateinit var printer: EscPosPrinter

    private lateinit var connection: BluetoothConnection
    private val _broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                /*BluetoothDevice.ACTION_ACL_CONNECTED -> {
                    val device =
                        intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    if (device != null && device.address == connection.device?.address) {
                        Log.d("Bluetooth", "Connected is connect ${device.address}")
                        //_printerState.tryEmit(PrinterState.Connected)
                    }
                }*/

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

    override suspend fun connect(): ResultType<Unit> {
        return makeCallDatabase {
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

            _printerState.emit(PrinterState.Connected)
        }.onError {
            _printerState.tryEmit(PrinterState.Exception(it))
        }

    }

    override fun disconnect() {
        printer.disconnectPrinter()
        _printerState.tryEmit(PrinterState.Disconnected)
    }

    override suspend fun print(document: DocumentElectronic):ResultType<Unit> = makeCallDatabase {

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


        // Si es dte y tiene timbre fiscal
        if (document.docType != DteType.ORDER_NOTE && !document.timbre.isNullOrEmpty()) {
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
                PrinterTextParserImg.bitmapToHexadecimalString(printer, monochromeBitmap)


            timbreHex?.let {
                printText.append("[C]<img>$timbreHex</img>\n")
                printText.append("[C]Timbre Electrónico S.I.I\n")
                printText.append("[C]Res. ${document.resolution}\n")
                printText.append("[C]Verifique en www.sii.cl\n")
            }

        } else {
            printText.append("[C]SIN VALIDEZ TRIBUTARIA\n")
        }
        printer.printFormattedText(printText.toString())
        _printerState.emit(PrinterState.Ready)
    }.onError {
        _printerState.tryEmit(PrinterState.Exception(it))
    }

}


