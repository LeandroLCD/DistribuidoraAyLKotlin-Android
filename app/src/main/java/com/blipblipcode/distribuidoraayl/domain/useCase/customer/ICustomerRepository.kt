package com.blipblipcode.distribuidoraayl.domain.useCase.customer

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import com.blipblipcode.distribuidoraayl.domain.models.customer.Route
import com.blipblipcode.distribuidoraayl.domain.models.customer.Rubro
import kotlinx.coroutines.flow.Flow

interface ICustomerRepository {

    /*Rutas*/
    fun getRoutes(): Flow<List<Route>>
    //suspend fun getRoute(uid:String): Route
    suspend fun createRoute(route: Route): ResultType<Unit>
    suspend fun updateRoute(route: Route): ResultType<Unit>

    /*

    * */

    /*Clientes*/
    suspend fun createOrUpdateCustomer(customer: Customer): ResultType<Unit>
    suspend fun getCustomers(): Flow<List<Customer>>
    fun getCustomerFlow(rut: String): Flow<Customer>
    suspend fun getCustomer(rut:String): ResultType<Customer>
    suspend fun deleteCustomer(rut:String): ResultType<Unit>

    /*Rubros*/
    suspend fun getRubros(): Flow<List<Rubro>>

    //Todo: Crear modelos de datos para regiones y comunas
    //fun getRegions(): Flow<List<Region>>

}