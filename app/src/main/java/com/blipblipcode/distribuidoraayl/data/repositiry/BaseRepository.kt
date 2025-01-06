package com.blipblipcode.distribuidoraayl.data.repositiry

import android.content.Context
import com.blipblipcode.distribuidoraayl.core.utils.isNetworkAvailable
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.throwable.NetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

open class BaseRepository(private val dispatcher: CoroutineDispatcher,private val context: Context) {

    val repositoryScope: CoroutineScope = CoroutineScope(dispatcher + SupervisorJob())

    suspend fun <T> makeCallNetwork(callNetwork: suspend ()-> T): ResultType<T> {
        return try {
            if(context.isNetworkAvailable()){
                withContext(dispatcher){
                    ResultType.Success(callNetwork())
                }
            }else{
                throw NetworkException()
            }

        }  catch (e: Throwable) {
            handleException(e)
        }

    }

    private fun <T> handleException(throwable: Throwable): ResultType<T> {

        val castException = when(throwable){
        is FirebaseAuthInvalidCredentialsException -> throwable
            is FirebaseAuthUserCollisionException -> throwable

            else -> throwable
         }
        return ResultType.Error(castException)
    }

}