package com.blipblipcode.distribuidoraayl.domain.di

import com.blipblipcode.distribuidoraayl.domain.useCase.customer.*
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class CustomerModule {
    @Binds
    abstract fun bindGetCustomers(useCase: GetCustomerUseCase): IGetCustomerUseCase

    @Binds
    abstract fun bindGetCustomerByRut(useCase: GetCustomerByRutUseCase): IGetCustomerByRutUseCase

    @Binds
    abstract fun bindCreateOrUpdateCustomer(useCase: CreateOrUpdateCustomerUseCase): ICreateOrUpdateCustomerUseCase

    @Binds
    abstract fun bindDeleteCustomer(useCase: DeleteCustomerUseCase): IDeleteCustomerUseCase

    @Binds
    abstract fun bindCreateRoute(useCase: CreateRouteUseCase): ICreateRouteUseCase

    @Binds
    abstract fun bindUpdateRoute(useCase: UpdateRouteUseCase): IUpdateRouteUseCase

    @Binds
    abstract fun bindGetRoutes(useCase: GetRoutesUseCase): IGetRoutesUseCase

    @Binds
    abstract fun bindGetRegions(useCase: GetRegionsUseCase): IGetRegionsUseCase

    @Binds
    abstract fun bindGetRubros(useCase: GetRubrosUseCase): IGetRubrosUseCase

}