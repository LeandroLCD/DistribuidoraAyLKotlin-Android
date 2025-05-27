package com.blipblipcode.distribuidoraayl.ui.sales.models

import androidx.compose.runtime.Stable
import com.blipblipcode.distribuidoraayl.domain.models.products.Product

@Stable
class ProductSelected(
    val product: Product,
    val isSelected: Boolean
)

