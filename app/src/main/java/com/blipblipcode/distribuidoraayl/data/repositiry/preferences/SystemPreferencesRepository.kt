package com.blipblipcode.distribuidoraayl.data.repositiry.preferences

import android.content.Context
import androidx.preference.PreferenceManager
import com.blipblipcode.distribuidoraayl.data.dto.of.CredentialOfDto
import com.blipblipcode.distribuidoraayl.data.repositiry.BaseRepository
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.preferences.CredentialOf
import com.blipblipcode.distribuidoraayl.domain.throwable.CredentialsNotConfiguredException
import com.blipblipcode.distribuidoraayl.domain.useCase.preferences.ISystemPreferencesRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class SystemPreferencesRepository @Inject constructor(
    dispatcher: CoroutineDispatcher, context: Context,
    private val remoteConfig: FirebaseRemoteConfig
):BaseRepository(dispatcher, context), ISystemPreferencesRepository {

    companion object{
        const val CREDENTIAL_OF = "credential_of"
    }

    private val gsonFactory = Gson()
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private var retry = 0

    override suspend fun syncCredentialOf(): ResultType<CredentialOf> {
        return makeCallNetwork {
            suspendCancellableCoroutine { continuation ->
                remoteConfig.fetchAndActivate().addOnCompleteListener{
                    if(it.isSuccessful){
                        val credentialOf = remoteConfig.getString(CREDENTIAL_OF)
                        preferences.edit().putString(CREDENTIAL_OF, credentialOf).apply()
                        continuation.resume(gsonFactory.fromJson(credentialOf, CredentialOfDto::class.java).mapToDomain())
                    }else{
                        continuation.cancel(it.exception)
                    }
                }.addOnFailureListener {exception ->
                    continuation.cancel(exception)
                }
            }
        }
    }

    override fun getCredentialOf(): CredentialOf? {
        return if(preferences.contains(CREDENTIAL_OF)){
            val credentialOf = preferences.getString(CREDENTIAL_OF, "")
            if(!credentialOf.isNullOrEmpty()){
                gsonFactory.fromJson(credentialOf, CredentialOfDto::class.java).mapToDomain()
            }else{
                repositoryScope.launch {
                    syncCredentialOf()
                }
                null
            }
        }else{
            if(retry == 3){
                throw CredentialsNotConfiguredException()
            }else{
                repositoryScope.launch {
                    syncCredentialOf()
                }
                retry++
                getCredentialOf()

            }
        }
    }
}