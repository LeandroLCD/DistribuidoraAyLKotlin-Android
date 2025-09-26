package com.blipblipcode.distribuidoraayl.ui.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi

class FileManager private constructor(
    private val context: Context,
    private var folderName: String? = null,
    private var subject: String? = null,
    private var body: String? = null,
    private var title: String = "Compartir archivos"
) {

    private val filesToSend = mutableListOf<Pair<String, String>>() // Pair<fileName, content>

    companion object {
        fun with(context: Context): FileManager {
            return FileManager(context)
        }
    }

    fun folder(name: String): FileManager = apply {
        this.folderName = name
    }

    fun subject(text: String): FileManager = apply {
        this.subject = text
    }

    fun body(text: String): FileManager = apply {
        this.body = text
    }

    fun title(text: String): FileManager = apply {
        this.title = text
    }

    fun addFile(fileName: String, content: String): FileManager = apply {
        filesToSend.add(fileName to content)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun send() {
        val uris = filesToSend.mapNotNull { (fileName, content) ->
            context.createTxtFileInDownloadsWithMediaStore(
                fileName = fileName,
                path = folderName,
                content = content
            )
        }

        if (uris.size == 1) {
            context.shareTxtFileByEmail(
                fileUri = uris.first(),
                title = title,
                subject = subject,
                body = body
            )
        } else if (uris.isNotEmpty()) {
            context.shareTxtFileByEmail(
                filesUri = uris,
                title = title,
                subject = subject,
                body = body
            )
        }
    }
}
