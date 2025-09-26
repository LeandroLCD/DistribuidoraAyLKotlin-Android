package com.blipblipcode.distribuidoraayl.data.dto.products

import com.blipblipcode.distribuidoraayl.core.local.entities.product.ProductEntity
import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.data.mapper.ToEntity
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

@Suppress("unused")
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
    var sku: String,
    @get:PropertyName("udm")
    @set:PropertyName("udm")
    var udm: ProductUdmDto,
    @get:PropertyName("barCode")
    @set:PropertyName("barCode")
    var barCode: String,
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String,
    @get:PropertyName("netPrice")
    @set:PropertyName("netPrice")
    var grossPrice: Double,
    @get:PropertyName("offer")
    @set:PropertyName("offer")
    var offer: OfferDto
): Mappable<Product>, ToEntity<ProductEntity> {
    constructor(): this("", null, "", "", "", ProductUdmDto(), "", "", 0.0, OfferDto(0.0, false))

    override fun mapToDomain(): Product {
        return Product(
            uid = uid,
            category = category?.mapToDomain(),
            brandId = brandId,
            description = description,
            sku = sku,
            udm = udm.mapToDomain(),
            barCode = barCode,
            name = name,
            grossPrice = grossPrice,
            offer = offer.mapToDomain()
        )
    }

    override fun mapToEntity(): ProductEntity {
        return ProductEntity(
            uid = uid,
            category = category?.mapToEntity(),
            brandId = brandId,
            description = description,
            sku = sku,
            udm = udm.mapToEntity(),
            barCode = barCode,
            name = name,
            grossPrice = grossPrice,
            offer = offer.mapToEntity()
        )
    }
}