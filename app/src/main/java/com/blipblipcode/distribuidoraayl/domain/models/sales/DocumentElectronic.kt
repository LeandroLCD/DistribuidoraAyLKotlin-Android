package com.blipblipcode.distribuidoraayl.domain.models.sales

import android.net.Uri

data class DocumentElectronic(
    val number: Long,
    val docType: DteType,
    val format: DocFormat,
    val token: String? = null,
    val resolution: Resolution?  = null,
    val timbre: String?  = null,
    val sale: Sale,
    val payment: Payment? = null,
    val uri: Uri
)