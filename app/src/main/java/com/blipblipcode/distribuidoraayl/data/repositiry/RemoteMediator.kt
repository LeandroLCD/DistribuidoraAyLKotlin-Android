package com.blipblipcode.distribuidoraayl.data.repositiry

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

abstract class RemoteMediator {

    abstract fun subscribeToCollection()

    abstract fun unsubscribeFromCollection()
}

fun <T> Flow<T>.remoteMediator(remote: RemoteMediator) = this.onStart {
    Log.d("ProductRemoteMediator", "onStart: subscribeToCollection")
    remote.subscribeToCollection()
}
    .onCompletion {
        Log.d("ProductRemoteMediator", "onCompletion: unsubscribeFromCollection")
        remote.unsubscribeFromCollection()
    }
