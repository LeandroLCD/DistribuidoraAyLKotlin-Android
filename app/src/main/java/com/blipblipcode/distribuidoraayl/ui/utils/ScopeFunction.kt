package com.blipblipcode.distribuidoraayl.ui.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import java.text.Normalizer
import java.text.NumberFormat
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

@RequiresApi(Build.VERSION_CODES.Q)
fun Context.createTxtFileInDownloadsWithMediaStore(fileName: String, path:String?, content: String): Uri? {
    val resolver = contentResolver
    val values = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        put(MediaStore.Downloads.MIME_TYPE, "text/plain")
        if(path != null){
            put(MediaStore.Downloads.RELATIVE_PATH, "Download/${path.removePrefix("/")}")
        }
        put(MediaStore.Downloads.IS_PENDING, 1)
    }

    val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    val itemUri = resolver.insert(collection, values)

    itemUri?.let { uri ->
        resolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(content.toByteArray())
        }

        values.clear()
        values.put(MediaStore.Downloads.IS_PENDING, 0)
        resolver.update(uri, values, null, null)
    }

    return itemUri
}

fun Context.shareFileByEmail(fileUri: Uri, title:String, typeFile: String = "text/plain") {
    val intent = Intent(Intent.ACTION_SEND).apply {

        type = typeFile

        putExtra(Intent.EXTRA_STREAM, fileUri)

        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    startActivity(Intent.createChooser(intent, title))
}

fun Context.shareTxtFileByEmail(fileUri: Uri, title:String, subject: String? = null, body: String? = null) {
    val intent = Intent(Intent.ACTION_SEND).apply {

        type = "text/plain"

        subject?.let {
            putExtra(Intent.EXTRA_SUBJECT, it)
        }

        body?.let {
            putExtra(Intent.EXTRA_TEXT, it)
        }

        putExtra(Intent.EXTRA_STREAM, fileUri)

        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    startActivity(Intent.createChooser(intent, title))
}
fun Context.shareTxtFileByEmail(filesUri: List<Uri>, title:String, subject: String? = null, body: String? = null) {
    val intent = Intent(Intent.ACTION_SEND).apply {

        type = "text/plain"

        subject?.let {
            putExtra(Intent.EXTRA_SUBJECT, it)
        }

        body?.let {
            putExtra(Intent.EXTRA_TEXT, it)
        }

        putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(filesUri))

        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    startActivity(Intent.createChooser(intent, title))
}

fun Context.readJsonFile(@RawRes rawResId: Int): String {
    return resources.openRawResource(rawResId)
        .bufferedReader()
        .use { it.readText() }
}

fun Int.toCurrency(): String {
    val format = NumberFormat.getInstance()
    return "${format.format(this)} $"
}