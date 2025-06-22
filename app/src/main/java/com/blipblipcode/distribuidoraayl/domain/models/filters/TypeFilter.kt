package com.blipblipcode.distribuidoraayl.domain.models.filters
sealed class TypeFilter(symbol: String) {

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
        data object Contains : Text("in")
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