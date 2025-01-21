package com.blipblipcode.distribuidoraayl.data.mapper

interface Mappable<T> {
    fun mapToDomain(): T
}