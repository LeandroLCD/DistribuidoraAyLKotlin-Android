package com.blipblipcode.distribuidoraayl.domain.di

import com.blipblipcode.distribuidoraayl.domain.useCase.products.IAddProductUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.ICreateCategoryUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IDeleteProductsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetCategoriesUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductBrandsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetUdmUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IUpdateProductUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.impl.AddProductUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.impl.CreateCategoryUseCaseUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.impl.DeleteProductsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.impl.GetCategoriesUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.impl.GetProductBrandsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.impl.GetProductUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.impl.GetProductsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.impl.GetUdmUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.impl.UpdateProductUseCase
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

    @Binds
    internal abstract fun bindAddProductUseCase(useCase: AddProductUseCase): IAddProductUseCase

    @Binds
    internal abstract fun bindGetProduct(useCase: GetProductUseCase): IGetProductUseCase

    @Binds
    internal abstract fun bindGetProducts(useCase: GetProductsUseCase): IGetProductsUseCase

    @Binds
    internal abstract fun bindCreateCategory(useCase: CreateCategoryUseCaseUseCase): ICreateCategoryUseCase

    @Binds
    internal abstract fun bindDeleteProduct(useCase: DeleteProductsUseCase): IDeleteProductsUseCase

    @Binds
    internal abstract fun bindUpdateProduct(useCase: UpdateProductUseCase): IUpdateProductUseCase



}