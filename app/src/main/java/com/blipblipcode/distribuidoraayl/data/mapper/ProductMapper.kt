package com.blipblipcode.distribuidoraayl.data.mapper

import com.blipblipcode.distribuidoraayl.data.dto.products.CategoryDto
import com.blipblipcode.distribuidoraayl.data.dto.products.CategoryProductDto
import com.blipblipcode.distribuidoraayl.data.dto.products.OfferDto
import com.blipblipcode.distribuidoraayl.data.dto.products.ProductBrandsDto
import com.blipblipcode.distribuidoraayl.data.dto.products.ProductDto
import com.blipblipcode.distribuidoraayl.data.dto.products.ProductUdmDto
import com.blipblipcode.distribuidoraayl.domain.models.products.ProductBrands
import com.blipblipcode.distribuidoraayl.domain.models.products.Category
import com.blipblipcode.distribuidoraayl.domain.models.products.Offer
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.blipblipcode.distribuidoraayl.domain.models.products.Udm

fun ProductBrands.toDto(): ProductBrandsDto {
    return ProductBrandsDto(uid, name)
}

fun Category.toDto(): CategoryDto {
    return CategoryDto(uid, name)
}

fun Category.toProductDto(): CategoryProductDto {
    return CategoryProductDto(uid, name)
}

fun Offer.toDto(): OfferDto {
    return OfferDto(percentage, isActive)
}

fun Udm.toDto(): ProductUdmDto {
    return ProductUdmDto(uid, name)
}

fun Product.toDto(): ProductDto {
    return ProductDto(
        uid = uid,
        category = category?.toProductDto(),
        brandId = brandId,
        description = description,
        sku = sku,
        udm = udm.toDto(),
        barCode = barCode,
        name = name,
        grossPrice = grossPrice,
        offer = offer.toDto()
    )
}