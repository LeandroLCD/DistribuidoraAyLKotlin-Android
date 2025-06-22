package com.blipblipcode.distribuidoraayl.domain.models.filters

data class DataFilter(
    val type: TypeFilter,
    val field: String,
    val value: String,
    val displayValue: String,
    val isVisible: Boolean = true
) {
    override fun toString(): String {
        return "$field : $displayValue"
    }
}