package com.blipblipcode.distribuidoraayl.core.local.entities.reportSale

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.blipblipcode.distribuidoraayl.domain.models.sales.DteType

@Entity(tableName = "sale_data", indices = [Index("uid", unique = true)])
data class SaleDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val uid: String? = null,
    val number: Int,
    val clientRut: String,
    val date: Long,
    val token: String?,
    @Embedded(prefix = "resolution_") val resolution: ResolutionEntity?,
    val timbre: String?,
    val isSynchronized: Boolean = false,
    val dteType: DteType
)