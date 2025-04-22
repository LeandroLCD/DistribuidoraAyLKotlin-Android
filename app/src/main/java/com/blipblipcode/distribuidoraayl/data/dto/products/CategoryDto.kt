package com.blipblipcode.distribuidoraayl.data.dto.products

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.products.Category
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

@Suppress("unused")
data class CategoryDto(
    @DocumentId
    var uid:String,
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name:String
): Mappable<Category> {
    constructor(): this( "", "")
    override fun mapToDomain(): Category {
        return Category(uid = uid, name = name)
    }
}
