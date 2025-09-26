package com.blipblipcode.distribuidoraayl.data.repositiry

import android.content.Context
import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
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
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

open class BaseFireStoreRepository(
    dispatcher: CoroutineDispatcher,
    context: Context,
    open val fireStore: FirebaseFirestore
):BaseRepository(dispatcher, context) {
    suspend inline fun <reified T> getDocumentDetail(
        collection: String,
        documentId: String,
    ): T? {
        val documentSnapshot = fireStore.collection(collection)
            .document(documentId)
            .get()
            .await()

        return documentSnapshot?.toObject<T>()
    }

    inline fun <reified T, R> getDocumentsFlow(collection: String) where  T : Mappable<R> =
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

    inline fun <reified T, R> getDocumentFlow(
        collection: String,
        documentId: String,
    ): Flow<R> where  T : Mappable<R> = callbackFlow {

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