package com.blipblipcode.distribuidoraayl.core.di.wok

import android.content.Context
import androidx.startup.Initializer
import com.blipblipcode.distribuidoraayl.core.di.wok.initializer.DependencyGraphInitializer
import com.blipblipcode.distribuidoraayl.domain.useCase.reportSale.IReportSaleRepository
import javax.inject.Inject

class ReportSaleInitializer: Initializer<IReportSaleRepository> {
    @Inject
    lateinit var repository:IReportSaleRepository

            override fun create(context: Context): IReportSaleRepository {
                InitializerEntryPoint.resolve(context).inject(this)
                return repository
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return mutableListOf(DependencyGraphInitializer::class.java)
    }

}