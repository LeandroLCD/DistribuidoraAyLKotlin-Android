package com.blipblipcode.distribuidoraayl.data.repositiry.auth

import android.content.Context
import android.content.SharedPreferences
import com.blipblipcode.distribuidoraayl.core.utils.KEY_PREFERENCES_AUTH
import com.blipblipcode.distribuidoraayl.core.utils.KEY_TIME_CACHE_VERIFIED
import com.blipblipcode.distribuidoraayl.data.mapper.toUser
import com.blipblipcode.distribuidoraayl.data.repositiry.BaseRepository
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.User
import com.blipblipcode.distribuidoraayl.domain.throwable.UnAuthenticationException
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IAuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume

class AuthRepository @Inject constructor(
    dispatcher: CoroutineDispatcher,
    context: Context,
    private val firebaseAuth: FirebaseAuth
) :
    BaseRepository(dispatcher, context), IAuthRepository {

    private val preferences: SharedPreferences = context.getSharedPreferences(KEY_PREFERENCES_AUTH, Context.MODE_PRIVATE)


    override fun getUser(): ResultType<User> {
        return firebaseAuth.currentUser.run {
            if (this != null) {
                ResultType.Success(this.toUser())
            } else {
                ResultType.Error(UnAuthenticationException())
            }
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): ResultType<User> {
        return makeCallNetwork {
            suspendCancellableCoroutine { continuation ->
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            continuation.resumeWith(Result.success(it.result.toUser()))
                        } else {
                            continuation.cancel(it.exception!!)
                        }
                    }
                    .addOnFailureListener {
                        continuation.cancel(it)
                    }
                    .addOnCanceledListener {
                        continuation.cancel()
                    }
            }
        }
    }

    override suspend fun createUserWithEmailAndPassword(email: String, password: String): ResultType<User> {
        return makeCallNetwork {
            suspendCancellableCoroutine { continuation ->
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        continuation.resume(result.toUser())
                    }
                    .addOnFailureListener { exception ->
                        continuation.cancel(exception)
                    }
                    .addOnCanceledListener {
                        continuation.cancel()
                    }
            }
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): ResultType<Unit> {
        return makeCallNetwork {
            suspendCancellableCoroutine { continuation ->
                firebaseAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener { exception ->
                        continuation.cancel(exception)
                    }
                    .addOnCanceledListener {
                        continuation.cancel()
                    }
            }
        }
    }

    override suspend fun sendEmailVerification(): ResultType<Unit> {
        return makeCallNetwork {
            val timeNaw = Date().time
            val lastTime = preferences.getLong(KEY_TIME_CACHE_VERIFIED, 0)
            val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)

            suspendCancellableCoroutine { continuation ->
                if (timeNaw.minus(lastTime) > cacheTimeout) {
                    firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                        preferences.edit().apply {
                            putLong(KEY_TIME_CACHE_VERIFIED, timeNaw)
                        }.apply()
                        continuation.resume(Unit)
                    }?.addOnFailureListener {
                        continuation.cancel(it)
                    }
                } else {
                    continuation.resume(Unit)
                }

            }

        }
    }

    override suspend fun reloadAccount(): ResultType<User> {
        return makeCallNetwork {
            suspendCancellableCoroutine { continuation ->
                if(firebaseAuth.currentUser != null){
                    firebaseAuth.currentUser!!.reload().addOnSuccessListener {
                        continuation.resume(firebaseAuth.currentUser!!.toUser())
                    }
                        .addOnFailureListener { exception ->
                            continuation.cancel(exception)
                        }
                        .addOnCanceledListener {
                            continuation.cancel()
                        }

                }else{
                    continuation.cancel(UnAuthenticationException())
                }
            }

        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}