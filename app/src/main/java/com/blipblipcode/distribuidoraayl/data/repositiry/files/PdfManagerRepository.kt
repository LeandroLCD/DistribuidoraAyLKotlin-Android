package com.blipblipcode.distribuidoraayl.data.repositiry.files

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Base64
import androidx.core.net.toUri
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.data.repositiry.BaseRepository
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocFormat
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocumentElectronic
import com.blipblipcode.distribuidoraayl.domain.models.sales.DteType
import com.blipblipcode.distribuidoraayl.domain.models.sales.Payment
import com.blipblipcode.distribuidoraayl.domain.models.sales.Sale
import com.blipblipcode.distribuidoraayl.domain.useCase.pdfManager.IPdfManagerRepository
import com.blipblipcode.distribuidoraayl.ui.utils.toCurrencyFormat
import com.blipblipcode.library.DateTime
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.layout.Document
import com.itextpdf.layout.Style
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.LineSeparator
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import kotlinx.coroutines.CoroutineDispatcher
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

class PdfManagerRepository @Inject constructor(
    private val context: Context,
    dispatcher: CoroutineDispatcher
) :
    BaseRepository(context = context, dispatcher = dispatcher), IPdfManagerRepository {
    private data class FileData(
        val outputStream: OutputStream,
        val uri: Uri
    )

    override suspend fun generatePreview(sale: Sale, isLetter: Boolean): ResultType<DocumentElectronic> {
        return makeCallDatabase {
            val uri = if (isLetter) {
               generateDteLetter(
                    sale = sale,
                    dteType = DteType.ORDER_NOTE,
                    number = 0,
                    resolution = "0 DE 2.014",
                    fiscalTimbre = "",
                   payment = Payment.Cash
                )
            } else {
                generateDte80mm(
                    sale = sale,
                    dteType = DteType.ORDER_NOTE,
                    number = 0,
                    resolution = "0 DE 2.014",
                    fiscalTimbre = "",
                    payment = Payment.Cash
                )
            }

            DocumentElectronic(
                number = sale.date.format("yyyyMMddHHmmss").toLong(),
                docType = DteType.ORDER_NOTE,
                timbre = "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",
                format = if(isLetter) DocFormat.LETTER else DocFormat.F80MM,
                sale = sale,
                payment = Payment.Cash,
                uri = uri
            )
        }
    }

    override fun generateDteLetter(
        sale: Sale,
        dteType: DteType,
        number: Long,
        resolution: String,
        fiscalTimbre: String,
        payment: Payment
    ): Uri {
        //region Crear archivo PDF
        val fileName = when (dteType) {
            DteType.ORDER_NOTE -> "Nota de Pedido ${sale.date.format("yyyyMMddHHmmss")}"
            DteType.INVOICE -> "Factura $number"
            else -> "Nota de Crédito $number"
        }

        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val appDir = File(downloadsDir, context.getString(R.string.app_name))
        if (appDir.exists()) {
            appDir.listFiles()?.forEach { it.delete() }
        } else {
            appDir.mkdirs()
        }

        // Luego creamos el nuevo archivo
        val file = File(appDir, "$fileName.pdf")
        val fileData = FileData(
            outputStream = FileOutputStream(file),
            uri = file.toUri()
        )

        //endregion


        //region Configurar documento
        val writer = PdfWriter(fileData.outputStream)
        val pdf = PdfDocument(writer)
        pdf.defaultPageSize = PageSize.LETTER
        pdf.addNewPage()
        val document = Document(pdf)

        document.setMargins(57f, 57f, 57f, 57f)
        //endregion

        //region Datos de la Factura

        // Datos del emisor
        val headerEmisor = Table(1).setMaxWidth(312f)
        headerEmisor.addCell(
            Cell().add(Paragraph(sale.eCommerce.companyName))
                .setFontSize(16f).setBorder(Border.NO_BORDER)
        )
        headerEmisor.addCell(
            Cell().add(Paragraph(sale.eCommerce.economicActivity))
                .setFontSize(11f).setBorder(Border.NO_BORDER)
        )
        headerEmisor.addCell(
            Cell().add(Paragraph(sale.eCommerce.addressOrigin))
                .setFontSize(11f).setBorder(Border.NO_BORDER)
        )
        document.add(headerEmisor)

        // Folio y tipo documento
        val headerFolio = Table(1)
        val dteString = when (dteType) {
            DteType.ORDER_NOTE -> "Nota de Pedido"
            DteType.INVOICE -> "Factura"
            else -> "Nota de crédito"
        }
        val folio = if (number == 0L) sale.date.format("yyyyMMddHHmmss") else number.toString()

        headerFolio.addCell(
            Cell().add(Paragraph("R.U.T.: ${sale.eCommerce.rut}"))
                .setFontSize(12f).setBorder(Border.NO_BORDER)
        )
        headerFolio.addCell(
            Cell().add(Paragraph(dteString))
                .setFontSize(12f).setBorder(Border.NO_BORDER)
        )
        headerFolio.addCell(
            Cell().add(Paragraph("N° $folio"))
                .setFontSize(12f).setBorder(Border.NO_BORDER)
        )

        headerFolio.setTextAlignment(TextAlignment.CENTER)
            .setFontColor(ColorConstants.RED)
            .setFixedPosition(383f, 660f, 169f)
            .setBorder(SolidBorder(ColorConstants.RED, 1f))
        document.add(headerFolio)

        val commune =
            Paragraph(if (number == 0L) "Sin Validez Tributaria" else "S.I.I. - ${sale.eCommerce.communeOrigin}")
        commune.setTextAlignment(TextAlignment.CENTER)
            .setFontColor(ColorConstants.RED)
            .setFixedPosition(383f, 640f, 169f)
        document.add(commune)

        // Línea separadora
        document.add(
            LineSeparator(SolidLine())
                .setMarginTop(30f)
                .setBackgroundColor(ColorConstants.BLACK)
        )

        // Datos del receptor
        val columnsReceptor = floatArrayOf(70f, 248f, 180f)
        val tableReceptor = Table(columnsReceptor)

        // Columna izquierda con titulos
        val columnLabels = Cell(5, 1)
        columnLabels.add(Paragraph("R.U.T:"))
        columnLabels.add(Paragraph("Señor (ES):"))
        columnLabels.add(Paragraph("Giro:"))
        columnLabels.add(Paragraph("Dirección:"))
        columnLabels.add(Paragraph("Comuna:"))
        columnLabels.setFontSize(10f)
        tableReceptor.addCell(columnLabels.setBorder(Border.NO_BORDER))

        // Columna  con datos del recepor
        val columnData = Cell(5, 1)
        columnData.add(Paragraph(sale.receiver.rut))
        columnData.add(Paragraph(sale.receiver.name.take(35)))
        columnData.add(
            Paragraph(
                sale.receiver.turn?.take(35)
                    ?: "Sin inicialización de actividades ante el SII".take(35)
            )
        )
        columnData.add(Paragraph(sale.receiver.address))
        columnData.add(Paragraph(sale.receiver.commune))
        columnData.setFontSize(10f)
        tableReceptor.addCell(columnData.setBorder(Border.NO_BORDER))

        // Columna derecha con fecha
        val fechaCell = Cell(1, 1)
            .add(
                Paragraph("${sale.date.day} de ${sale.date.monthName()}, del ${sale.date.year}")
                    .setTextAlignment(TextAlignment.RIGHT)
            )
        tableReceptor.addCell(fechaCell.setBorder(Border.NO_BORDER))

        document.add(tableReceptor.setRelativePosition(0f, 5f, 0f, 0f))

        // Tabla de items
        val columnsItems = floatArrayOf(50f, 288f, 25f, 60f, 75f)
        val tableItems = Table(columnsItems)

        // Encabezado de items
        val headerStyle = Style()
            .setBackgroundColor(ColorConstants.BLACK)
            .setFontColor(ColorConstants.WHITE)
            .setFontSize(8f)

        tableItems.addCell(Cell().add(Paragraph("Código")).addStyle(headerStyle))
        tableItems.addCell(Cell().add(Paragraph("Descripción")).addStyle(headerStyle))
        tableItems.addCell(Cell().add(Paragraph("Cant")).addStyle(headerStyle))
        tableItems.addCell(
            Cell().add(Paragraph("Precio")).setTextAlignment(TextAlignment.RIGHT)
                .addStyle(headerStyle)
        )
        tableItems.addCell(
            Cell().add(Paragraph("Sub Total")).setTextAlignment(TextAlignment.RIGHT)
                .addStyle(headerStyle)
        )

        // Estilo para las celdas de items
        val itemStyle = Style()
            .setBorderBottom(Border.NO_BORDER)
            .setBorderLeft(SolidBorder(ColorConstants.BLACK, 1f))
            .setBorderRight(Border.NO_BORDER)
            .setBorderTop(Border.NO_BORDER)
            .setFontSize(8f)

        val itemStyleRight = Style()
            .setBorderBottom(Border.NO_BORDER)
            .setBorderLeft(SolidBorder(ColorConstants.BLACK, 1f))
            .setBorderRight(Border.NO_BORDER)
            .setBorderTop(Border.NO_BORDER)
            .setFontSize(8f)
            .setBorderRight(SolidBorder(ColorConstants.BLACK, 1f))

        // Agregar items
        sale.items.forEachIndexed { index, item ->
            val isLast = index == sale.items.size - 1
            val cellHeight = if (isLast) 14f * (23 - sale.items.size) else 14f

            tableItems.addCell(
                Cell().add(Paragraph(item.sku))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setHeight(cellHeight)
                    .addStyle(itemStyle)
            )

            tableItems.addCell(
                Cell().add(Paragraph(item.name))
                    .setHeight(cellHeight)
                    .addStyle(itemStyle)
            )

            tableItems.addCell(
                Cell().add(Paragraph(item.quantity.toString()))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setHeight(cellHeight)
                    .addStyle(itemStyle)
            )

            tableItems.addCell(
                Cell().add(Paragraph("${item.price}"))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setHeight(cellHeight)
                    .addStyle(itemStyle)
            )

            tableItems.addCell(
                Cell().add(Paragraph("${item.price * item.quantity}"))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setHeight(cellHeight)
                    .addStyle(itemStyleRight)
            )
        }

        document.add(
            tableItems
                .setBorderBottom(SolidBorder(ColorConstants.BLACK, 1f))
                .setRelativePosition(0f, 30f, 0f, 0f)
        )

        // Totales
        val columnsTotals = floatArrayOf(60f, 40f)
        val tableTotals = Table(columnsTotals)
        val totalStyle = Style()
            .setHeight(14f)
            .setFontSize(8f)
            .setBorder(SolidBorder(ColorConstants.BLACK, 1f))

        tableTotals.addCell(Cell().add(Paragraph("Monto Neto:")).addStyle(totalStyle))
        tableTotals.addCell(
            Cell().add(Paragraph("${sale.totals.netAmount}"))
                .setTextAlignment(TextAlignment.RIGHT)
                .addStyle(totalStyle)
        )

        tableTotals.addCell(
            Cell().add(Paragraph("IVA ${sale.totals.tax}%:")).addStyle(totalStyle)
        )
        tableTotals.addCell(
            Cell().add(Paragraph("${sale.totals.taxAmount}"))
                .setTextAlignment(TextAlignment.RIGHT)
                .addStyle(totalStyle)
        )

        tableTotals.addCell(Cell().add(Paragraph("Monto Total:")).addStyle(totalStyle))
        tableTotals.addCell(
            Cell().add(Paragraph("${sale.totals.total}"))
                .setTextAlignment(TextAlignment.RIGHT)
                .addStyle(totalStyle)
        )

        document.add(tableTotals.setFixedPosition(415f, 77f, 140f))

        // Timbre fiscal si existe y es factura
        if (dteType == DteType.INVOICE && fiscalTimbre.isNotEmpty()) {
            val timbreTable = Table(floatArrayOf(150f))
            val timbreStyle = Style()
                .setFontSize(6f)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER)

            val imageBytes = Base64.decode(fiscalTimbre, Base64.DEFAULT)
            val image = Image(ImageDataFactory.create(imageBytes))
                .setWidth(UnitValue.createPercentValue(100f))

            val timbreCell = Cell(3, 1)
                .add(image)
                .addStyle(timbreStyle)
            timbreTable.addCell(timbreCell)

            val notesCell = Cell(1, 1)
                .add(Paragraph("Timbre Electrónico S.I.I"))
                .add(Paragraph("Res. $resolution"))
                .add(Paragraph("Verifique documento en www.sii.cl")).addStyle(timbreStyle)
            timbreTable.addCell(notesCell)

            document.add(timbreTable.setFixedPosition(57f, 57f, 100f))
        }
        //endregion

        document.close()
        fileData.outputStream.close()

        return fileData.uri

    }

    override fun generateDte80mm(
        sale: Sale,
        dteType: DteType,
        number: Long,
        resolution: String,
        fiscalTimbre: String,
        payment: Payment
    ): Uri {
        //region Crear archivo PDF
        val fileName = when (dteType) {
            DteType.ORDER_NOTE -> "Nota de Pedido ${sale.date.format("yyyyMMddHHmm")}"
            DteType.INVOICE -> "Factura Electronica $number"
            else -> "Nota de Crédito $number"
        }

        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val appDir = File(downloadsDir, context.getString(R.string.app_name))
        if (appDir.exists()) {
            appDir.listFiles()?.forEach { it.delete() }
        } else {
            appDir.mkdirs()
        }

        val file = File(appDir, "$fileName.pdf")
        val fileData = FileData(
            outputStream = FileOutputStream(file),
            uri = file.toUri()
        )
        //endregion

        //region Configurar documento
        val writer = PdfWriter(fileData.outputStream)
        val pdf = PdfDocument(writer)
        val h = (500f + (10f.times(sale.items.size)) + if(fiscalTimbre.isNotEmpty()) 40f else 0f).coerceIn(500f, 792f)
        pdf.defaultPageSize = PageSize(226f, h)
        pdf.addNewPage()
        val document = Document(pdf)

        document.setMargins(10f, 10f, 10f, 10f)
        //endregion

        //region Datos de la Factura

        val headerFolio = Table(1)
            .setWidth(206f)
            .setBorder(SolidBorder(ColorConstants.BLACK, 1f))


        val dteString = when (dteType) {
            DteType.ORDER_NOTE -> "Nota de Pedido"
            DteType.INVOICE -> "Factura"
            else -> "Nota de crédito"
        }


        val folio = if (number == 0L) sale.date.format("yyyyMMddHHmmss") else number.toString()

        headerFolio.addCell(
            Cell().add(Paragraph("R.U.T.: ${sale.eCommerce.rut}"))
                .setFontSize(8f).setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER)
        )
        headerFolio.addCell(
            Cell().add(Paragraph(dteString))
                .setFontSize(8f).setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER)
        )
        headerFolio.addCell(
            Cell().add(Paragraph("N° $folio"))
                .setFontSize(8f).setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER)
        )
        document.add(headerFolio)

        val headerEmisor = Table(1).setWidth(203f).setBorder(Border.NO_BORDER).setMarginTop(22f)
        headerEmisor.addCell(
            Cell().add(Paragraph(sale.eCommerce.companyName))
                .setFontSize(10f).setBorder(Border.NO_BORDER)
        )
        headerEmisor.addCell(
            Cell().add(Paragraph(sale.eCommerce.businessLine))
                .setFontSize(8f).setBorder(Border.NO_BORDER)
        )
        headerEmisor.addCell(
            Cell().add(Paragraph(sale.eCommerce.addressOrigin))
                .setFontSize(8f).setBorder(Border.NO_BORDER)
        )
        headerEmisor.addCell(
            Cell().add(Paragraph(sale.eCommerce.phone))
                .setFontSize(8f).setBorder(Border.NO_BORDER)
        )
        document.add(headerEmisor)

        val line = LineSeparator(SolidLine())
            .setBackgroundColor(ColorConstants.BLACK)
        document.add(line)

        val headerReceiver= Table(1).setWidth(203f).setBorder(Border.NO_BORDER).setMarginTop(4f)
        headerReceiver.addCell(
            Cell().add(Paragraph("FECHA DE EMISION: ${sale.date.format("yyyy-MM-dd")}"))
                .setFontSize(10f).setBorder(Border.NO_BORDER)
        )
        headerReceiver.addCell(
            Cell().add(Paragraph("R.U.T.: ${sale.receiver.rut}"))
                .setFontSize(10f).setBorder(Border.NO_BORDER)
        )
        headerReceiver.addCell(
            Cell().add(Paragraph("SEÑOR(ES): ${sale.receiver.name}"))
                .setFontSize(10f).setBorder(Border.NO_BORDER)
        )
        headerReceiver.addCell(
            Cell().add(Paragraph("GIRO: ${sale.receiver.turn}"))
                .setFontSize(8f).setBorder(Border.NO_BORDER)
        )
        headerReceiver.addCell(
            Cell().add(Paragraph("DIRECCION: ${sale.receiver.address}"))
                .setFontSize(8f).setBorder(Border.NO_BORDER)
        )
        headerReceiver.addCell(
            Cell().add(Paragraph(sale.eCommerce.phone))
                .setFontSize(8f).setBorder(Border.NO_BORDER)
        )
        document.add(headerReceiver)

        document.add(line)

        document.add(Paragraph("FORMA DE PAGO: ${if (payment is Payment.Cash) "CONTADO" else "CREDITO"}").setFontSize(8f)
            .setFontSize(8f).setMarginTop(4f))
        document.add(line)

        val tableItemsHeader = Table(floatArrayOf(103f, 103f)).setBorder(Border.NO_BORDER).setMarginTop(4f)
        tableItemsHeader.addCell(
            Cell().add(Paragraph("ARTICULO")).setFontSize(8f)
                .setBorder(Border.NO_BORDER)
        )
        tableItemsHeader.addCell(
            Cell().add(Paragraph("VALOR")).setFontSize(8f).setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
        )
        document.add(tableItemsHeader)
        document.add(line)
        val tableItems = Table(floatArrayOf(103f, 103f)).setBorder(Border.NO_BORDER)
        sale.items.forEach { item ->
            tableItems.addCell(
                Cell(1, 2).add(Paragraph("${item.sku}-${item.name}")).setFontSize(8f)
                    .setBorder(Border.NO_BORDER)
            )
            tableItems.addCell(
                Cell().add(Paragraph("${item.quantity} X ${item.price}")).setFontSize(8f)
                    .setBorder(Border.NO_BORDER)
            )
            tableItems.addCell(
                Cell().add(
                    Paragraph(item.price.times(item.quantity).toDouble().toCurrencyFormat())
                        .setFontSize(8f)
                        .setTextAlignment(TextAlignment.RIGHT)
                )
                    .setBorder(Border.NO_BORDER)
            )
        }
        document.add(tableItems)
        document.add(line)
        val tableTotals = Table(floatArrayOf(103f, 103f)).setBorder(Border.NO_BORDER).setMarginTop(22f)
        tableTotals.addCell(Cell().add(
            Paragraph("Monto Neto:")).setFontSize(8f)
            .setBorder(Border.NO_BORDER)
        )
        tableTotals.addCell(
            Cell().add(
                Paragraph("${sale.totals.netAmount}")).setFontSize(8f)
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER)
        )
        tableTotals.addCell(
            Cell().add(Paragraph("IVA ${sale.totals.tax}%:")).setFontSize(8f)
                .setBorder(Border.NO_BORDER)
        )
        tableTotals.addCell(
            Cell().add(Paragraph("${sale.totals.taxAmount}")).setFontSize(8f)
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER)
        )
        tableTotals.addCell(
            Cell().add(Paragraph("Monto Total:")).setFontSize(8f)
                .setBorder(Border.NO_BORDER))
        tableTotals.addCell(
            Cell().add(Paragraph("${sale.totals.total}")).setFontSize(8f)
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER)
        )
        document.add(tableTotals)

        // Timbre fiscal si existe y es factura
        if (dteType == DteType.INVOICE && fiscalTimbre.isNotEmpty()) {
            val timbreTable = Table(floatArrayOf(206f))
            val timbreStyle = Style()
                .setFontSize(6f)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER)

            val imageBytes = Base64.decode(fiscalTimbre, Base64.DEFAULT)
            val image = Image(ImageDataFactory.create(imageBytes))
                .setWidth(UnitValue.createPercentValue(100f))

            val timbreCell = Cell(3, 1)
                .add(image)
                .addStyle(timbreStyle)
            timbreTable.addCell(timbreCell)

            val notesCell = Cell(1, 1)
                .add(Paragraph("Timbre Electrónico S.I.I"))
                .add(Paragraph("Res. $resolution"))
                .add(Paragraph("Verifique documento en www.sii.cl")).addStyle(timbreStyle)
            timbreTable.addCell(notesCell)

            document.add(timbreTable.setFixedPosition(10f, 10f, 206f))
        }
        //endregion

        document.close()
        fileData.outputStream.close()

        return fileData.uri
    }

    private fun DateTime.monthName(): String {
        return when (this.month) {
            1 -> "enero"
            2 -> "febrero"
            3 -> "marzo"
            4 -> "abril"
            5 -> "mayo"
            6 -> "junio"
            7 -> "julio"
            8 -> "agosto"
            9 -> "septiembre"
            10 -> "octubre"
            11 -> "noviembre"
            12 -> "diciembre"
            else -> ""
        }
    }
}