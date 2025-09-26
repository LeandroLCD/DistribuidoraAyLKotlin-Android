package com.blipblipcode.distribuidoraayl.domain.models.customer

data class Customer (
    val commune:String,
    val regionId:String,
    val country:String = "CL",
    val address:String,
    val companyName:String,
    val rut:String,
    val rubro: Rubro,
    val branches:List<Branch>?,
    val activities:List<Activity>?,
    val phone:String,
    val registrationDate:String,
    val birthDate:String,
    val routeId:String?,
    val rutCode:String
){
    constructor() : this(
        rut = "",
        commune = "",
        regionId = "",
        rubro = Rubro(0, "", ""),
        country = "CL",
        address = "",
        companyName = "",
        branches = null,
        activities = null,
        phone = "",
        registrationDate = "",
        birthDate = "",
        routeId = "",
        rutCode = ""
    )
}
