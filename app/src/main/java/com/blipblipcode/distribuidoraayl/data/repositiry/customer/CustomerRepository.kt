package com.blipblipcode.distribuidoraayl.data.repositiry.customer

import android.content.Context
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.data.dto.CustomerDto
import com.blipblipcode.distribuidoraayl.data.dto.RouteDto
import com.blipblipcode.distribuidoraayl.data.dto.RubroDto
import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.data.mapper.toDto
import com.blipblipcode.distribuidoraayl.data.repositiry.BaseRepository
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
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
    context: Context,
    private val dispatcher: CoroutineDispatcher,
    private val fireStore: FirebaseFirestore
    ): BaseRepository(dispatcher, context), ICustomerRepository {

        companion object{
            const val CUSTOMER = "customerCatalogue"
            const val ROUTE = "routes"
            const val RUBRO = "rubros"
        }

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

    override suspend fun getCustomers(): Flow<List<Customer>> {
        return getDocumentsFlow<CustomerDto, Customer>(RUBRO).flowOn(dispatcher)
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

    override suspend fun getRubros(): Flow<List<Rubro>> {
        return getDocumentsFlow<RubroDto, Rubro>(RUBRO).flowOn(dispatcher)
    }

    private suspend inline fun <reified T> getDocumentDetail(
        collection: String,
        documentId: String,
    ): T? {
        val documentSnapshot = fireStore.collection(collection)
            .document(documentId)
            .get()
            .await()

        return documentSnapshot?.toObject<T>()
    }

    private inline fun <reified T, R> getDocumentsFlow(collection: String) where  T : Mappable<R> =
        callbackFlow {
            val event = EventListener<QuerySnapshot> { value, error ->
                if (error != null) {
                    close(error)
                    return@EventListener
                }
                val items = value?.documents?.mapNotNull {
                    it.toObject<T>()?.mapToDomain()
                }
                if (items != null) {
                    trySend(items)
                }
            }

            val listenerRegistration = fireStore.collection(collection).addSnapshotListener(event)

            awaitClose {
                listenerRegistration.remove()
            }
        }

    private inline fun <reified T, R> getDocumentFlow(
        collection: String,
        documentId: String,
    ): Flow<R>  where  T : Mappable<R> = callbackFlow {

        val event = EventListener<DocumentSnapshot> { value, error ->
            if (error != null) {
                close(error)
                return@EventListener
            }
            value?.toObject<T>()?.let {
                trySend(it.mapToDomain())
            }
        }

        val reference = fireStore.collection(collection).document(documentId)
        val querySnapshot = reference.get().await()
        if (!querySnapshot.exists()) {
            // El documento no existe, crea un nuevo documento con el ID de usuario
            //reference.set(hashMapOf("parametro" to "valor")).await()
            cancel(cause = CancellationException("Document not found"))
        }

        val listenerRegistration = reference
            .addSnapshotListener(event)

        awaitClose {
            listenerRegistration.remove()
        }
    }


}