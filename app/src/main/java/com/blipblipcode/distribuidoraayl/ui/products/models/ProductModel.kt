package com.blipblipcode.distribuidoraayl.ui.products.models

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.products.Category
import com.blipblipcode.distribuidoraayl.domain.models.products.Offer
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.blipblipcode.distribuidoraayl.domain.models.products.Udm
import com.blipblipcode.distribuidoraayl.domain.throwable.RequiredFieldException
import com.blipblipcode.distribuidoraayl.ui.auth.login.models.DataField
import kotlin.math.ceil

@Stable
@Immutable
data class ProductModel(
    val iva: Double,
    val uid: String = "",
    val category: DataField<Category?> = DataField(null, isError = true, errorException = RequiredFieldException()),
    val brandId: DataField<String?> = DataField(null, isError = true, errorException = RequiredFieldException()),
    val description: DataField<String> = DataField(""),
    val sku: DataField<String> = DataField("", isError = true, errorException = RequiredFieldException()),
    val udm: DataField<Udm?> = DataField(null, isError = true, errorException = RequiredFieldException()),
    val barCode: DataField<String> = DataField(""),
    val name: DataField<String> = DataField("", isError = true, errorException = RequiredFieldException()),
    val grossPrice: DataField<Double> = DataField(0.0, isError = true, errorException = RequiredFieldException()),
    val offer: DataField<Offer> = DataField(Offer(0.0, isActive = false))
):Mappable<Product> {
    val netPrice: Double
        get() = if (offer.value.isActive)
            ceil(
                (grossPrice.value - grossPrice.value.times(
                    offer.value.percentage.div(
                        100
                    )
                )) * 100
            ) / 100
        else
            ceil(grossPrice.value * 100) / 100

    val total: Double
        get() = ceil((netPrice.times(iva.plus(1))) * 100) / 100


    fun rememberIsValid(): State<Boolean> {
        return derivedStateOf {
            name.isValid() &&  sku.isValid() &&
            udm.isValid() && grossPrice.isValid() &&
                    category.isValid() && brandId.isValid()
        }
    }

    override fun mapToDomain(): Product {
        return Product(
            uid = uid,
            name = name.value,
            sku = sku.value,
            description = description.value,
            barCode = barCode.value,
            grossPrice = grossPrice.value,
            category = category.value,
            udm = udm.value!!,
            offer = offer.value,
            brandId = brandId.value.orEmpty(),
        )
    }
}