package com.blipblipcode.distribuidoraayl.core.local.entities.product

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.products.Category

class CategoryEntity(val categoryId: String, val categoryName: String):
    Mappable<Category> {
    override fun mapToDomain(): Category {
        return Category(
            uid = categoryId,
            name = categoryName
        )
    }
}
