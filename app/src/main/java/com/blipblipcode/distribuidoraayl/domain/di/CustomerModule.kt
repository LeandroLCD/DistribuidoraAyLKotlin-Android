package com.blipblipcode.distribuidoraayl.domain.di

import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICreateOrUpdateCustomerUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICreateRouteUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IDeleteCustomerUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetCustomerByRutUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetCustomerFlowUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetCustomersUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetRegionsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetRoutesUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetRubrosUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IUpdateRouteUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl.CreateOrUpdateCustomerUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl.CreateRouteUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl.DeleteCustomerUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl.GetCustomerByRutUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl.GetCustomerFlowUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl.GetCustomersUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl.GetRegionsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl.GetRoutesUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl.GetRubrosUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl.UpdateRouteUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class CustomerModule {
    @Binds
    abstract fun bindGetCustomers(useCase: GetCustomersUseCase): IGetCustomersUseCase

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

    @Binds
    abstract fun bindGetCustomerFlow(useCase: GetCustomerFlowUseCase): IGetCustomerFlowUseCase

}