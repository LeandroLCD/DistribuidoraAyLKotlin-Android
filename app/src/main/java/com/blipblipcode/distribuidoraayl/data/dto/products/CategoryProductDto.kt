package com.blipblipcode.distribuidoraayl.data.dto.products

import com.blipblipcode.distribuidoraayl.core.local.entities.product.CategoryEntity
import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.data.mapper.ToEntity
import com.blipblipcode.distribuidoraayl.domain.models.products.Category
import com.google.firebase.firestore.PropertyName

@Suppress("unused")
class CategoryProductDto(
    @get:PropertyName("uid")
    @set:PropertyName("uid")
    var uid:String,
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name:String
): Mappable<Category>, ToEntity<CategoryEntity> {
    constructor(): this( "", "")
    override fun mapToDomain(): Category {
        return Category(uid, name)
    }

    override fun mapToEntity(): CategoryEntity {
        return CategoryEntity(categoryId = uid, categoryName = name)
    }
}