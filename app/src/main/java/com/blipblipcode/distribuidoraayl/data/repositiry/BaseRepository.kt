package com.blipblipcode.distribuidoraayl.data.repositiry

import android.content.Context
import com.blipblipcode.distribuidoraayl.core.di.utils.isNetworkAvailable
import com.blipblipcode.distribuidoraayl.data.dto.of.error.ErrorRootDto
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.throwable.BackendErrorException
import com.blipblipcode.distribuidoraayl.domain.throwable.NetworkException
import com.blipblipcode.distribuidoraayl.domain.throwable.PasswordIsNotValidException
import com.blipblipcode.distribuidoraayl.domain.throwable.UserDeletedException
import com.blipblipcode.distribuidoraayl.domain.throwable.UserDisabledException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import retrofit2.HttpException

open class BaseRepository(
    private val dispatcher: CoroutineDispatcher,
    private val context: Context
) {
    private val gsonFactory = Gson()
    val repositoryScope: CoroutineScope = CoroutineScope(dispatcher + SupervisorJob())

    suspend fun <T> makeCallNetwork(callNetwork: suspend () -> T): ResultType<T> {
        return try {
            if (context.isNetworkAvailable()) {
                withContext(dispatcher) {
                    ResultType.Success(callNetwork())
                }
            } else {
                throw NetworkException()
            }

        } catch (e: Throwable) {
            handleException(e)
        }

    }

    suspend fun <T> makeCallDatabase(callNetwork: suspend () -> T): ResultType<T> {
        return try {
            withContext(dispatcher) {
                ResultType.Success(callNetwork())
            }

        } catch (e: Throwable) {
            handleException(e)
        }

    }


    private fun <T> handleException(throwable: Throwable): ResultType<T> {

        val castException = when (throwable) {
            is FirebaseAuthInvalidCredentialsException -> {
                if (throwable.errorCode == "ERROR_INVALID_CREDENTIAL") {
                    PasswordIsNotValidException()
                } else {
                    throwable
                }
            }
            is FirebaseNetworkException ->{
                NetworkException()
            }

            is FirebaseAuthUserCollisionException -> throwable
            is FirebaseAuthInvalidUserException -> {
                if (throwable.errorCode == "ERROR_USER_DISABLED") {
                    UserDisabledException()
                } else if (throwable.errorCode == "ERROR_USER_NOT_FOUND") {
                    UserDeletedException()
                } else {
                    throwable
                }
            }

            is HttpException -> handleHttpException(throwable)
            else -> throwable
        }
        return ResultType.Error(castException)
    }

    private fun handleHttpException(exception: HttpException): Throwable {
        return when (exception.code()) {
            400 -> {
                val errorBody = exception.response()?.errorBody()?.string()
                val dto = gsonFactory.fromJson(errorBody, ErrorRootDto::class.java)
                return BackendErrorException(dto.error.details.joinToString(", "))
            }

            else -> exception
        }

    }

}