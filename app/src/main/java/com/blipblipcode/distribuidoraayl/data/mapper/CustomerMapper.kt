package com.blipblipcode.distribuidoraayl.data.mapper

import com.blipblipcode.distribuidoraayl.data.dto.ActivityDto
import com.blipblipcode.distribuidoraayl.data.dto.BranchDto
import com.blipblipcode.distribuidoraayl.data.dto.CustomerDto
import com.blipblipcode.distribuidoraayl.data.dto.RouteDto
import com.blipblipcode.distribuidoraayl.data.dto.RubroDto
import com.blipblipcode.distribuidoraayl.domain.models.customer.Activity
import com.blipblipcode.distribuidoraayl.domain.models.customer.Branch
import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import com.blipblipcode.distribuidoraayl.domain.models.customer.Route
import com.blipblipcode.distribuidoraayl.domain.models.customer.Rubro


fun Route.toDto(): RouteDto {
    return RouteDto(
        uid = uid,
        name = name
    )
}
fun Customer.toDto(): CustomerDto{
    return CustomerDto(
        commune = commune,
        address = address,
        activities = activities?.map { it.toDto() },
        branches = branches?.map { it.toDto() },
        companyName = companyName,
        country = country,
        phone = phone,
        registrationDate = registrationDate,
        birthDate = birthDate,
        regionId = regionId,
        rut = rut,
        rutCode = rutCode,
        routeId = routeId,
        sapCode = sapCode,
        rubro = rubro.toDto()
    )

}
fun Rubro.toDto(): RubroDto {
    return RubroDto(
        id = id,
        soc = soc,
        description = description
    )
}

private fun Activity.toDto(): ActivityDto {
    return ActivityDto(
        turn = turn,
        code = code,
        name = name,
        isMainActivity = isMainActivity
    )
}
private fun Branch.toDto(): BranchDto {
    return BranchDto(
        city = city,
        code = code,
        commune = commune,
        address = address,
        phone = phone.orEmpty(),
        isHouseMatrix = isHouseMatrix
    )
}
