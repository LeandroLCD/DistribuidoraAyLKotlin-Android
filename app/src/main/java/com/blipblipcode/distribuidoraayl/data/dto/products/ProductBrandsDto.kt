package com.blipblipcode.distribuidoraayl.data.dto.products

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.products.ProductBrands
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class ProductBrandsDto(
    @DocumentId
    var uid: String,
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String
) : Mappable<ProductBrands> {
    constructor() : this("", "")

    override fun mapToDomain(): ProductBrands {
        return ProductBrands(uid, name)
    }
}