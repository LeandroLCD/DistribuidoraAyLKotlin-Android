package com.blipblipcode.distribuidoraayl.ui.reports


import com.google.gson.annotations.SerializedName

data class Report(
    @SerializedName("AmountIva")
    val amountIva: Int,
    @SerializedName("Fecha")
    val fecha: String,
    @SerializedName("Invoice")
    val invoice: String,
    @SerializedName("Iva")
    val iva: Double,
    @SerializedName("Name")
    val name: String,
    @SerializedName("PdfBase64")
    val pdfBase64: String,
    @SerializedName("ProdAyL")
    val prodAyL: List<Sale>?,
    @SerializedName("Rut")
    val rut: String,
    @SerializedName("Sales")
    val sales: List<Sale>?,
    @SerializedName("SellerCode")
    val sellerCode: String,
    @SerializedName("TotalSale")
    val totalSale: Int
){

}