package com.blipblipcode.distribuidoraayl.ui.widgets.pdfViewer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.annotation.RawRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

enum class PdfListDirection {
    HORIZONTAL, VERTICAL
}

@ExperimentalFoundationApi
@Composable
fun PdfViewer(
    @RawRes pdfResId: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF909090),
    pageColor: Color = Color.White,
    listDirection: PdfListDirection = PdfListDirection.VERTICAL,
    fallbackWidget: @Composable () -> Unit = {},
    loadingListener: (
        isLoading: Boolean,
        currentPage: Int?,
        maxPage: Int?,
    ) -> Unit = { _, _, _ -> }
) {
    val context = LocalContext.current
    PdfViewer(
        pdfStream = context.resources.openRawResource(pdfResId),
        modifier = modifier,
        pageColor = pageColor,
        backgroundColor = backgroundColor,
        listDirection = listDirection,
        fallbackWidget = fallbackWidget,
        loadingListener = loadingListener,
    )
}

@ExperimentalFoundationApi
@Composable
fun PdfViewer(
    pdfStream: InputStream,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF909090),
    pageColor: Color = Color.White,
    listDirection: PdfListDirection = PdfListDirection.VERTICAL,
    fallbackWidget: @Composable () -> Unit = {},
    loadingListener: (
        isLoading: Boolean,
        currentPage: Int?,
        maxPage: Int?,
    ) -> Unit = { _, _, _ -> }
) {
    PdfViewer(
        pdfStream = pdfStream,
        modifier = modifier,
        backgroundColor = backgroundColor,
        listDirection = listDirection,
        loadingListener = loadingListener,
        fallbackWidget = fallbackWidget
    ) { lazyState, image ->
        PdfPaging(
            painter = BitmapPainter(image),
            pageColor = pageColor,
            scrollState = lazyState,
            backgroundColor = backgroundColor
        )
    }
}

@SuppressLint("Recycle")
@ExperimentalFoundationApi
@Composable
fun PdfViewer(
    uri: Uri,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF909090),
    onError: @Composable (Throwable) -> Unit = {},
    listDirection: PdfListDirection = PdfListDirection.VERTICAL,
    loadingListener: (
        isLoading: Boolean,
        currentPage: Int?,
        maxPage: Int?,
    ) -> Unit = { _, _, _ -> },
    fallbackWidget: @Composable () -> Unit = {},
    page: @Composable (PagerState, ImageBitmap) -> Unit = { pagerState, image ->
        PdfPaging(
            painter = BitmapPainter(image),
            scrollState = pagerState,
            backgroundColor = backgroundColor
        )
    },
) {
    val context = LocalActivity.current
    val stream = context?.contentResolver?.openInputStream(uri)
    if (stream == null) {
        onError(Throwable("File not avadible"))
    } else {
        PdfViewer(
            pdfStream = stream,
            modifier = modifier,
            backgroundColor = backgroundColor,
            listDirection = listDirection,
            loadingListener = loadingListener,
            page = page,
            fallbackWidget = fallbackWidget
        )
    }
}

@ExperimentalFoundationApi
@Composable
fun PdfViewer(
    @RawRes pdfResId: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF909090),
    listDirection: PdfListDirection = PdfListDirection.VERTICAL,
    loadingListener: (
        isLoading: Boolean,
        currentPage: Int?,
        maxPage: Int?,
    ) -> Unit = { _, _, _ -> },
    page: @Composable (PagerState, ImageBitmap) -> Unit,
    fallbackWidget: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    PdfViewer(
        pdfStream = context.resources.openRawResource(pdfResId),
        modifier = modifier,
        backgroundColor = backgroundColor,
        listDirection = listDirection,
        loadingListener = loadingListener,
        page = page,
        fallbackWidget = fallbackWidget
    )
}

@ExperimentalFoundationApi
@Composable
fun PdfViewer(
    pdfStream: InputStream,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF909090),
    listDirection: PdfListDirection = PdfListDirection.VERTICAL,
    fallbackWidget: @Composable () -> Unit = {},
    loadingListener: (
        isLoading: Boolean,
        currentPage: Int?,
        maxPage: Int?,
    ) -> Unit = { _, _, _ -> },
    paddingValues: PaddingValues = PaddingValues(vertical = 20.dp, horizontal = 16.dp),
    page: @Composable (PagerState, ImageBitmap) -> Unit
) {
    val context = LocalContext.current
    val pagePaths = remember {
        mutableStateListOf<String>()
    }
    val showFallBackWidget = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(true) {
        if (pagePaths.isEmpty()) {
            val paths = context.loadPdf(inputStream = pdfStream, loadingListener = loadingListener)
            paths?.let {
                pagePaths.addAll(paths)
            } ?: run {
                showFallBackWidget.value = true
            }
        }
    }
    if (showFallBackWidget.value) {
        fallbackWidget()
    } else {
        val pagerState = rememberPagerState(pageCount = { pagePaths.size })

        when (listDirection) {
            PdfListDirection.HORIZONTAL ->
                HorizontalPager(
                    modifier = modifier.fillMaxSize().background(backgroundColor),
                    state = pagerState,
                    contentPadding = paddingValues,
                    pageSpacing = 16.dp
                ) { page ->

                    var imageBitmap by remember {
                        mutableStateOf<ImageBitmap?>(null)
                    }
                    val path = pagePaths[page]
                    LaunchedEffect(page) {
                        imageBitmap = BitmapFactory.decodeFile(path).asImageBitmap()
                    }
                    imageBitmap?.let { page(pagerState, it) }

                }

            PdfListDirection.VERTICAL ->
                VerticalPager(
                    modifier = modifier.fillMaxSize().background(backgroundColor),
                    state = pagerState,
                    contentPadding = paddingValues,
                    pageSpacing = 16.dp
                ) { page ->
                    var imageBitmap by remember {
                        mutableStateOf<ImageBitmap?>(null)
                    }
                    LaunchedEffect(page) {
                        val path = pagePaths[page]
                        imageBitmap = BitmapFactory.decodeFile(path).asImageBitmap()
                    }
                    imageBitmap?.let { page(pagerState, it) }

                }
        }
    }
}

data class PageSize(
    val width: Int,
    val height: Int
)

suspend fun Context.loadPdf(
    inputStream: InputStream,
    size: PageSize = PageSize(width = 1240, height = 1754),
    loadingListener: (
        isLoading: Boolean,
        currentPage: Int?,
        maxPage: Int?
    ) -> Unit = { _, _, _ -> }
): List<String>? = withContext(Dispatchers.Default) {
    try {
        loadingListener(true, null, null)
        val outputDir = cacheDir
        val tempFile = File.createTempFile("temp", "pdf", outputDir)
        tempFile.mkdirs()
        tempFile.deleteOnExit()
        val outputStream = FileOutputStream(tempFile)
        copy(inputStream, outputStream)
        val input = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
        val renderer = PdfRenderer(input)
        (0 until renderer.pageCount).map { pageNumber ->
            loadingListener(true, pageNumber.plus(1), renderer.pageCount)
            val file = File.createTempFile("PDFpage$pageNumber", "png", outputDir)
            file.mkdirs()
            file.deleteOnExit()
            val page = renderer.openPage(pageNumber)
            val bitmap = createBitmap(size.width, size.height)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
            Log.i("PDF_VIEWER", "Loaded page $pageNumber")
            file.absolutePath.also { Log.d("TESTE", it) }
        }.also {
            loadingListener(false, null, null)
            renderer.close()
        }
    } catch (e: Exception) {
        Log.i("PDF_VIEWER Exception", e.toString())
        loadingListener(false, null, null)
        return@withContext null
    }
}

@Throws(IOException::class)
private fun copy(source: InputStream, target: OutputStream) {
    val buf = ByteArray(8192)
    var length: Int
    while (source.read(buf).also { length = it } > 0) {
        target.write(buf, 0, length)
    }
}