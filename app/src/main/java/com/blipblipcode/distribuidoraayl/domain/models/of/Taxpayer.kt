package com.blipblipcode.distribuidoraayl.domain.models.of

import com.blipblipcode.distribuidoraayl.domain.models.customer.Activity
import com.blipblipcode.distribuidoraayl.domain.models.customer.Branch

data class Taxpayer (
    val activities: List<Activity>,
    val commune: String,
    val address: String,
    val email: Any?,
    val company: String,
    val rut: String,
    val branches: List<Branch>,
    val phone: String?
)