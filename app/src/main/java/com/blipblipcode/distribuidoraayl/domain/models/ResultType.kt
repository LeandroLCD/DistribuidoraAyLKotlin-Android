package com.blipblipcode.distribuidoraayl.domain.models

sealed class ResultType<T> {
    data class Success<T>(val data: T) : ResultType<T>()
    data class Error<T>(val exception: Throwable) : ResultType<T>()
}

suspend inline fun <T : Any> ResultType<T>.onSuccess(crossinline action: suspend (T) -> Unit): ResultType<T> =
        apply{
            if(this is ResultType.Success){
                action(data)
            }
        }

suspend inline fun <T : Any> ResultType<T>.onError(crossinline action: suspend (Throwable) -> Unit): ResultType<T> =
    apply{
        if(this is ResultType.Error){
            action(exception)
        }
    }