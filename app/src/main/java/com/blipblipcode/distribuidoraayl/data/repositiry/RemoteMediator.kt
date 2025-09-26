package com.blipblipcode.distribuidoraayl.data.repositiry

import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

abstract class RemoteMediator {

    internal abstract val mediatorScope: CoroutineScope

    abstract var event: ListenerRegistration?

    abstract fun subscribeToCollection()

    abstract fun unsubscribeFromCollection()
}

fun <T> Flow<T>.remoteMediator(remote: RemoteMediator) = this.onStart {
    remote.subscribeToCollection()
    }.onCompletion {
        remote.unsubscribeFromCollection()
    }
