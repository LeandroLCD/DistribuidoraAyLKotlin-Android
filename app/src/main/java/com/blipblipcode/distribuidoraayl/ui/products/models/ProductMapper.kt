package com.blipblipcode.distribuidoraayl.ui.products.models

import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.blipblipcode.distribuidoraayl.ui.auth.login.models.DataField

fun Product.toModel(iva:Double):ProductModel{
    return ProductModel(
        uid = uid,
        iva = iva,
        name = DataField(name),
        description = DataField(description),
        category = DataField(category),
        offer = DataField(offer),
        grossPrice = DataField(grossPrice),
        sku = DataField(sku),
        barCode = DataField(barCode),
        udm = DataField(udm),
        brandId = DataField(brandId)
    )
}