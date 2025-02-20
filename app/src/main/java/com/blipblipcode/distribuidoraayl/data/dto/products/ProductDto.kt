package com.blipblipcode.distribuidoraayl.data.dto.products

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class ProductDto(
    @DocumentId
    var uid: String,
    @get:PropertyName("category")
    @set:PropertyName("category")
    var category: CategoryProductDto?,
    @get:PropertyName("brandId")
    @set:PropertyName("brandId")
    var brandId: String,
    @get:PropertyName("description")
    @set:PropertyName("description")
    var description: String,
    @get:PropertyName("sku")
    @set:PropertyName("sku")
    var sku: Int,
    @get:PropertyName("udm")
    @set:PropertyName("udm")
    var udm: String,
    @get:PropertyName("barCode")
    @set:PropertyName("barCode")
    var barCode: String,
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String,
    @get:PropertyName("netPrice")
    @set:PropertyName("netPrice")
    var netPrice: Double,
    @get:PropertyName("offer")
    @set:PropertyName("offer")
    var offer: OfferDto
): Mappable<Product> {
    constructor(): this("", null, "", "", 0, "", "", "", 0.0, OfferDto(0.0, false))

    override fun mapToDomain(): Product {
        return Product(
            uid = uid,
            category = category?.mapToDomain(),
            brandId = brandId,
            description = description,
            sku = sku,
            udm = udm,
            barCode = barCode,
            name = name,
            netPrice = netPrice,
            offer = offer.mapToDomain()
        )
    }
}