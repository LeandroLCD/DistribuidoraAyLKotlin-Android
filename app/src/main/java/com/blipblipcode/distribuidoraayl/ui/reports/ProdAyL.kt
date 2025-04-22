package com.blipblipcode.distribuidoraayl.ui.reports


import com.google.gson.annotations.SerializedName

data class ProdAyL(
    @SerializedName("ClientRut")
    val clientRut: String,
    @SerializedName("DateDte")
    val dateDte: String,
    @SerializedName("DateSale")
    val dateSale: String,
    @SerializedName("DistributorCode")
    val distributorCode: String,
    @SerializedName("Dte")
    val dte: String,
    @SerializedName("Itms")
    val itms: Int,
    @SerializedName("OfficeCode")
    val officeCode: String,
    @SerializedName("Price")
    val price: Int,
    @SerializedName("PriceNeto")
    val priceNeto: Int,
    @SerializedName("ProductCode")
    val productCode: String,
    @SerializedName("Quantity")
    val quantity: Int,
    @SerializedName("ReportSaleId")
    val reportSaleId: Int,
    @SerializedName("SellerCode")
    val sellerCode: String,
    @SerializedName("SubTotal")
    val subTotal: Int,
    @SerializedName("Udm")
    val udm: String
)