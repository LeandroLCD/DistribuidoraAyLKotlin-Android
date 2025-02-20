package com.blipblipcode.distribuidoraayl.data.repositiry.customer

import android.content.Context
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.data.dto.customer.CustomerDto
import com.blipblipcode.distribuidoraayl.data.dto.customer.RegionDto
import com.blipblipcode.distribuidoraayl.data.dto.customer.RouteDto
import com.blipblipcode.distribuidoraayl.data.dto.customer.RubroDto
import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.data.mapper.toDto
import com.blipblipcode.distribuidoraayl.data.repositiry.BaseFireStoreRepository
import com.blipblipcode.distribuidoraayl.data.repositiry.BaseRepository
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import com.blipblipcode.distribuidoraayl.domain.models.customer.Region
import com.blipblipcode.distribuidoraayl.domain.models.customer.Route
import com.blipblipcode.distribuidoraayl.domain.models.customer.Rubro
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject


class CustomerRepository @Inject constructor(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
    override val fireStore: FirebaseFirestore
    ): BaseFireStoreRepository(dispatcher, context, fireStore), ICustomerRepository {

        companion object{
            const val CUSTOMER = "customerCatalogue"
            const val ROUTE = "routes"
            const val RUBRO = "rubros"
            const val REGIONS = "regions"
        }
    /*Scrips para insertar datos de forma masiva a FireStore*/
    /*
    suspend fun setRegions(): ResultType<Unit> {
        val gsonFactory = Gson()
        val json = context.resources.openRawResource(R.raw.regions).bufferedReader().use { it.readText() }

        return makeCallNetwork {
            val ref = fireStore.collection(REGIONS)
            val batch = fireStore.batch()
            val regions = gsonFactory.fromJson(json, RegionsDto::class.java)
            regions.regions.forEach {
                batch.set(ref.document(it.id), it)
            }
            batch.commit().await()
        }
    }

    suspend fun setRubros(): ResultType<Unit> {
        val gsonFactory = Gson()
        val json = context.resources.openRawResource(R.raw.rubros).bufferedReader().use { it.readText() }

        return makeCallNetwork {
            val ref = fireStore.collection(RUBRO)
            val batch = fireStore.batch()
            val rubros = gsonFactory.fromJson(json, RubrosDto::class.java)
            rubros.rubros.forEach {
                batch.set(ref.document(), it)
            }
            batch.commit().await()
        }
    }
    */

    override fun getRoutes(): Flow<List<Route>> {
        return getDocumentsFlow<RouteDto, Route>(ROUTE).flowOn(dispatcher)
    }

    override suspend fun createRoute(route: Route): ResultType<Unit> {
        return makeCallNetwork {
            fireStore.collection(ROUTE).add(route.toDto()).await()
        }
    }

    override suspend fun updateRoute(route: Route): ResultType<Unit> {
        return makeCallNetwork {
            fireStore.collection(ROUTE).document(route.uid).set(route.toDto()).await()
        }
    }

    override suspend fun createOrUpdateCustomer(customer: Customer): ResultType<Unit> {
        return makeCallNetwork {
            fireStore.collection(CUSTOMER).document(customer.rut).set(customer.toDto()).await()
        }
    }

    override fun getCustomers(): Flow<List<Customer>> {
        return getDocumentsFlow<CustomerDto, Customer>(CUSTOMER).flowOn(dispatcher)
    }


    override suspend fun getCustomer(rut: String): ResultType<Customer> {
        return makeCallNetwork {
            getDocumentDetail<CustomerDto>(CUSTOMER, rut)!!.mapToDomain()
        }
    }

    override fun getCustomerFlow(rut: String): Flow<Customer> {
         return   getDocumentFlow<CustomerDto, Customer>(CUSTOMER, rut)
    }

    override suspend fun deleteCustomer(rut: String): ResultType<Unit> {
        return makeCallNetwork {
            fireStore.collection(CUSTOMER).document(rut).delete().await()
        }
    }

    override fun getRegions(): Flow<List<Region>> {
        return getDocumentsFlow<RegionDto, Region>(REGIONS).flowOn(dispatcher)
    }

    override fun getRubros(): Flow<List<Rubro>> {
        return getDocumentsFlow<RubroDto, Rubro>(RUBRO).flowOn(dispatcher)
    }


}