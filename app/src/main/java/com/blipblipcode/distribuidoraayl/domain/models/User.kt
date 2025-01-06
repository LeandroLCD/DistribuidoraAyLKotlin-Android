package com.blipblipcode.distribuidoraayl.domain.models

data class User (
    val uid:String,
    val email:String,
    val isEmailVerified:Boolean,
)