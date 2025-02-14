package com.blipblipcode.distribuidoraayl.ui.customer.models

import androidx.compose.runtime.Stable

@Stable
data class DataFilter(val type:TypeFilter, val field:String, val value:String, val displayValue:String){
    override fun toString(): String {
        return "$field : $displayValue"
    }
}

@Stable
sealed class TypeFilter(val symbol: String) {

    // Filtros para texto
    sealed class Text(symbol: String) : TypeFilter(symbol) {
        data object Equals : Text("=")
        data object Contains : Text("in")
    }

    // Filtros para números
    sealed class Number(symbol: String) : TypeFilter(symbol) {
        data object GreaterThan : Number(">")
        data object LessThan : Number("<")
        data object Equals : Number("=")
    }

    // Filtros para fechas
    sealed class Date(symbol: String) : TypeFilter(symbol) {
        data object GreaterThan : Date(">")
        data object LessThan : Date("<")
        data object Equals : Date("=")
        data object Between : Date("&")
    }

    // Filtros para booleanos
    sealed class Boolean(symbol: String) : TypeFilter(symbol) {
        data object Equals : Boolean("==")
    }
}