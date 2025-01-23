package com.blipblipcode.distribuidoraayl.data.dto.customer

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

@Suppress("unused")
data class CustomerDto (
    @get:DocumentId
    var rut:String,
    @get:PropertyName("rubro")
    @set:PropertyName("rubro")
    var rubro: RubroDto,
    @set:PropertyName("commune")
    @get:PropertyName("commune")
    var commune:String,
    @get:PropertyName("regionId")
    @set:PropertyName("regionId")
    var regionId:String,
    @get:PropertyName("country")
    @set:PropertyName("country")
    var country:String = "CL",
    @get:PropertyName("address")
    @set:PropertyName("address")
    var address:String,
    @get:PropertyName("companyName")
    @set:PropertyName("companyName")
    var companyName:String,
    @get:PropertyName("branches")
    @set:PropertyName("branches")
    var branches:List<BranchDto>?,
    @get:PropertyName("activities")
    @set:PropertyName("activities")
    var activities:List<ActivityDto>?,
    @get:PropertyName("phone")
    @set:PropertyName("phone")
    var phone:String,
    @get:PropertyName("registrationDate")
    @set:PropertyName("registrationDate")
    var registrationDate:String,
    @get:PropertyName("birthDate")
    @set:PropertyName("birthDate")
    var birthDate:String,
    @get:PropertyName("routeId")
    @set:PropertyName("routeId")
    var routeId:String?,
    @set:PropertyName("rutCode")
    @get:PropertyName("rutCode")
    var rutCode:String,
    @set:PropertyName("sapCode")
    @get:PropertyName("sapCode")
    var sapCode:String?
): Mappable<Customer> {

    constructor() : this( rut="", commune="", regionId="", country="", address="", rubro = RubroDto(), companyName="", branches = listOf(), activities = listOf(), phone="", registrationDate="", birthDate="", routeId="", rutCode="", sapCode="")

    override fun mapToDomain(): Customer {
        return Customer(
            rut = rut,
            commune = commune,
            regionId = regionId,
            country = country,
            address = address,
            companyName = companyName,
            branches = branches?.map { it.mapToDomain() },
            activities = activities?.map { it.mapToDomain() },
            phone = phone,
            registrationDate = registrationDate,
            birthDate = birthDate,
            routeId = routeId,
            rutCode = rutCode,
            sapCode = sapCode,
            rubro = rubro.mapToDomain()
        )
    }
}