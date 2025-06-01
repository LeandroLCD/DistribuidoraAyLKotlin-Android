package com.blipblipcode.distribuidoraayl.data.repositiry.product

import android.util.Log
import androidx.room.withTransaction
import com.blipblipcode.distribuidoraayl.core.local.room.DataBaseApp
import com.blipblipcode.distribuidoraayl.core.local.room.dao.ProductDao
import com.blipblipcode.distribuidoraayl.data.dto.products.ProductDto
import com.blipblipcode.distribuidoraayl.data.repositiry.RemoteMediator
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductRemoteMediator @Inject constructor(
    dispatcher: CoroutineDispatcher,
    private val firestore: FirebaseFirestore,
    private val database: DataBaseApp
): RemoteMediator() {

    private val mediatorScope: CoroutineScope = CoroutineScope(dispatcher + SupervisorJob())

    private lateinit var event: ListenerRegistration

    override fun subscribeToCollection() {
        val listener = EventListener<QuerySnapshot> { value, error ->
            if (error != null) {
                return@EventListener
            }
            val items = value?.documents?.mapNotNull {
                it.toObject<ProductDto>()?.mapToEntity()
            }
            if (items != null) {
                Log.d("ProductRemoteMediator", "subscribeToCollection: $items")
                mediatorScope.launch {
                    database.withTransaction {
                        database.productDao().apply {
                            deleteAll()
                            insert(items)
                        }
                    }
                }
            }
        }
        event = firestore.collection(ProductsRepository.CATALOGUE).addSnapshotListener(listener)
    }

    override fun unsubscribeFromCollection() {
        if (::event.isInitialized) {
            event.remove()
        }
    }
}