package com.blipblipcode.distribuidoraayl.data.repositiry.pdf

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
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
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
                .setFontSize(16f).setBold().setBorder(Border.NO_BORDER)
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
                .setFontSize(12f).setBold().setBorder(Border.NO_BORDER)
        )
        headerFolio.addCell(
            Cell().add(Paragraph(dteString))
                .setFontSize(12f).setBold().setBorder(Border.NO_BORDER)
        )
        headerFolio.addCell(
            Cell().add(Paragraph("N° $folio"))
                .setFontSize(12f).setBold().setBorder(Border.NO_BORDER)
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
        columnLabels.add(Paragraph("R.U.T:").setBold())
        columnLabels.add(Paragraph("Señor (ES):").setBold())
        columnLabels.add(Paragraph("Giro:").setBold())
        columnLabels.add(Paragraph("Dirección:").setBold())
        columnLabels.add(Paragraph("Comuna:").setBold())
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
                    .setBold()
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
            .setBold()

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

        tableTotals.addCell(Cell().add(Paragraph("Monto Neto:").setBold()).addStyle(totalStyle))
        tableTotals.addCell(
            Cell().add(Paragraph("${sale.totals.netAmount}"))
                .setTextAlignment(TextAlignment.RIGHT)
                .addStyle(totalStyle)
        )

        tableTotals.addCell(
            Cell().add(Paragraph("IVA ${sale.totals.tax}%:").setBold()).addStyle(totalStyle)
        )
        tableTotals.addCell(
            Cell().add(Paragraph("${sale.totals.taxAmount}"))
                .setTextAlignment(TextAlignment.RIGHT)
                .addStyle(totalStyle)
        )

        tableTotals.addCell(Cell().add(Paragraph("Monto Total:").setBold()).addStyle(totalStyle))
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
                .setFontSize(10f).setBold().setBorder(Border.NO_BORDER)
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
                .setFontSize(10f).setBold().setBorder(Border.NO_BORDER)
        )
        headerReceiver.addCell(
            Cell().add(Paragraph("R.U.T.: ${sale.receiver.rut}"))
                .setFontSize(10f).setBold().setBorder(Border.NO_BORDER)
        )
        headerReceiver.addCell(
            Cell().add(Paragraph("SEÑOR(ES): ${sale.receiver.name}"))
                .setFontSize(10f).setBold().setBorder(Border.NO_BORDER)
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
            Cell().add(Paragraph("ARTICULO")).setFontSize(8f).setBold()
                .setBorder(Border.NO_BORDER)
        )
        tableItemsHeader.addCell(
            Cell().add(Paragraph("VALOR")).setFontSize(8f).setBold().setBorder(Border.NO_BORDER)
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
            Paragraph("Monto Neto:").setBold()).setFontSize(8f)
            .setBorder(Border.NO_BORDER)
        )
        tableTotals.addCell(
            Cell().add(
                Paragraph("${sale.totals.netAmount}")).setFontSize(8f).setBold()
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER)
        )
        tableTotals.addCell(
            Cell().add(Paragraph("IVA ${sale.totals.tax}%:").setBold()).setFontSize(8f)
                .setBorder(Border.NO_BORDER)
        )
        tableTotals.addCell(
            Cell().add(Paragraph("${sale.totals.taxAmount}")).setFontSize(8f).setBold()
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER)
        )
        tableTotals.addCell(
            Cell().add(Paragraph("Monto Total:").setBold()).setFontSize(8f)
                .setBorder(Border.NO_BORDER))
        tableTotals.addCell(
            Cell().add(Paragraph("${sale.totals.total}")).setFontSize(8f).setBold()
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