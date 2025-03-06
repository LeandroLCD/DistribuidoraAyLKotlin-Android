package com.blipblipcode.distribuidoraayl.domain.di

import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetCategoriesUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductBrandsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetUdmUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.impl.GetCategoriesUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.impl.GetProductBrandsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.impl.GetUdmUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class ProductModule {

    @Binds
    internal abstract fun bindProductBrands(useCase: GetProductBrandsUseCase): IGetProductBrandsUseCase

    @Binds
    internal abstract fun bindGetCategoriesUseCase(useCase: GetCategoriesUseCase): IGetCategoriesUseCase

    @Binds
    internal abstract fun bindGetUdmUseCase(useCase: GetUdmUseCase): IGetUdmUseCase


}