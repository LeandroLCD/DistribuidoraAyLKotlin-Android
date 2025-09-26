package com.blipblipcode.distribuidoraayl.core.repository

import com.blipblipcode.distribuidoraayl.data.repositiry.auth.AuthRepository
import com.blipblipcode.distribuidoraayl.data.repositiry.customer.CustomerRepository
import com.blipblipcode.distribuidoraayl.data.repositiry.of.OpenFacturaRepository
import com.blipblipcode.distribuidoraayl.data.repositiry.files.PdfManagerRepository
import com.blipblipcode.distribuidoraayl.data.repositiry.files.PrinterRepository
import com.blipblipcode.distribuidoraayl.data.repositiry.preferences.SystemPreferencesRepository
import com.blipblipcode.distribuidoraayl.data.repositiry.product.ProductsRepository
import com.blipblipcode.distribuidoraayl.data.repositiry.reportSale.ReportSaleRepository
import com.blipblipcode.distribuidoraayl.data.worker.SyncUpRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IAuthRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IOpenFacturaRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.pdfManager.IPdfManagerRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.preferences.ISystemPreferencesRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.printer.IPrinterRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IProductsRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.reportSale.IReportSaleRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.worker.ISyncUpRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideAuthRepository(authRepository: AuthRepository): IAuthRepository {
        return authRepository
    }

    @Singleton
    @Provides
    fun provideCustomerRepository(customerRepository: CustomerRepository): ICustomerRepository {
        return customerRepository
    }

    @Singleton
    @Provides
    fun provideOpenFacturaRepository(openFacturaRepository: OpenFacturaRepository): IOpenFacturaRepository {
        return openFacturaRepository
    }

    @Singleton
    @Provides
    fun provideSystemPreferencesRepository(systemPreferencesRepository: SystemPreferencesRepository): ISystemPreferencesRepository {
        return systemPreferencesRepository
    }

    @Singleton
    @Provides
    fun provideProductRepository(productRepository: ProductsRepository): IProductsRepository {
        return productRepository
    }

    @Singleton
    @Provides
    fun providePdfManagerRepository(pdfManagerRepository: PdfManagerRepository): IPdfManagerRepository {
        return pdfManagerRepository
    }

    @Singleton
    @Provides
    fun providePrinterRepository(printerRepository: PrinterRepository): IPrinterRepository {
        return printerRepository
    }

    @Singleton
    @Provides
    fun provideReportSaleRepository(reportSaleRepository: ReportSaleRepository): IReportSaleRepository {
        return reportSaleRepository
    }

    @Singleton
    @Provides
    fun provideSyncUpRepository(syncUpRepository: SyncUpRepository): ISyncUpRepository {
        return syncUpRepository
    }
}