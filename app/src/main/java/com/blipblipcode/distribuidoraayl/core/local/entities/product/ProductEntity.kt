package com.blipblipcode.distribuidoraayl.core.local.entities.product

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.products.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val uid: String = "",
    @Embedded val category: CategoryEntity?,
    val brandId: String,
    val description: String,
    val sku: String,
    @Embedded val udm: UdmEntity,
    val barCode: String,
    val name: String,
    val grossPrice: Double,
    @Embedded val offer: OfferEntity
):Mappable<Product> {
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
}