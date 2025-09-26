package com.blipblipcode.distribuidoraayl.ui.sales.models

import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import kotlin.math.roundToInt

@Stable
class ProductQuantity(
    val product: Product,
    val quantity: Int = 1,
    val isOffer: Boolean,
    val onChangedOffer: (Boolean) -> Unit,
    val onChangedQuantity: (Int) -> Unit
){
    val totalPrice =  derivedStateOf {
        val netPrice = if (isOffer && product.offer.percentage > 0f) {
            val discount = product.grossPrice * (product.offer.percentage / 100f)
            product.grossPrice - discount
        } else {
            product.grossPrice
        }
        (netPrice * quantity).roundToInt()
    }

    fun netPrice() = if (isOffer && product.offer.percentage > 0f) {
        val discount = product.grossPrice * (product.offer.percentage / 100f)
        product.grossPrice - discount
    } else {
        product.grossPrice
    }.roundToInt()

}