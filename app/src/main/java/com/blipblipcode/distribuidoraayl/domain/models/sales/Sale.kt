package com.blipblipcode.distribuidoraayl.domain.models.sales

import com.blipblipcode.distribuidoraayl.domain.models.preferences.ECommerce
import com.blipblipcode.library.DateTime

data class Sale(
    val date: DateTime,
    val eCommerce: ECommerce,
    val receiver: ClientReceiver,
    val items:List<SalesItem>,
    val totals: Totals
)

