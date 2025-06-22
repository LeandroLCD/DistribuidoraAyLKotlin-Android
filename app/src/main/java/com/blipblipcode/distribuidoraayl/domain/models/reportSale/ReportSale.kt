package com.blipblipcode.distribuidoraayl.domain.models.reportSale


import com.blipblipcode.distribuidoraayl.domain.models.sales.ClientReceiver
import com.blipblipcode.distribuidoraayl.domain.models.sales.DteType
import com.blipblipcode.distribuidoraayl.domain.models.sales.SalesItem
import com.blipblipcode.distribuidoraayl.domain.models.sales.Totals
import com.blipblipcode.library.DateTime

data class ReportSale(
    val id: Long,
    val uid: String?,
    val number: Int,
    val dteType: DteType,
    val client: ClientReceiver,
    val date: DateTime,
    val token: String?,
    val resolution: Resolution?,
    val timbre: String?,
    val isSynchronized: Boolean,
    val items: List<SalesItem>,
    val totals: Totals
)
