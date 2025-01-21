package com.blipblipcode.distribuidoraayl.data.dto

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.customer.Branch
import com.google.firebase.firestore.PropertyName

@Suppress("unused")
data class BranchDto (
    @get:PropertyName("city")
    @set:PropertyName("city")
    var city: String,
    @get:PropertyName("code")
    @set:PropertyName("code")
    var code: Int,
    @get:PropertyName("commune")
    @set:PropertyName("commune")
    var commune: String,
    @get:PropertyName("address")
    @set:PropertyName("address")
    var address: String,
    @get:PropertyName("phone")
    @set:PropertyName("phone")
    var phone: String,
    @get:PropertyName("isHouseMatrix")
    @set:PropertyName("isHouseMatrix")
    var isHouseMatrix:Boolean = false
): Mappable<Branch> {
    constructor() : this(city="", code=0, commune="", address="", phone="", isHouseMatrix = false)

    override fun mapToDomain(): Branch {
        return Branch(city, code, commune, address, phone, isHouseMatrix)
    }
}