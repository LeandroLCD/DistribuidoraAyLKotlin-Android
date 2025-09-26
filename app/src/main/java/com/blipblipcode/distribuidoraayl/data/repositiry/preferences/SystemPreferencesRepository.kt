package com.blipblipcode.distribuidoraayl.data.repositiry.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.blipblipcode.distribuidoraayl.data.dto.ecommerce.ECommerceDto
import com.blipblipcode.distribuidoraayl.data.dto.of.CredentialOfDto
import com.blipblipcode.distribuidoraayl.data.repositiry.BaseRepository
import com.blipblipcode.distribuidoraayl.domain.models.preferences.ECommerce
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.preferences.CredentialOf
import com.blipblipcode.distribuidoraayl.domain.useCase.preferences.ISystemPreferencesRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class SystemPreferencesRepository @Inject constructor(
    dispatcher: CoroutineDispatcher, context: Context,
    private val remoteConfig: FirebaseRemoteConfig,
    private val preferences: DataStore<Preferences>,
    private val gsonFactory: Gson
):BaseRepository(dispatcher, context), ISystemPreferencesRepository {

    private var cachedCredentials: CredentialOf? = null

    companion object{
        const val CREDENTIAL_OF = "credential_of"
        const val ECOMMERCE_KEY = "ecommerce"
        val CREDENTIAL_KEY = stringPreferencesKey(CREDENTIAL_OF)
        val ECOMMERCE_KEY_PREFERENCE = stringPreferencesKey(ECOMMERCE_KEY)
    }
    init {
        // Sincronización en segundo plano
        repositoryScope.launch {
            syncCredentialOf()
            syncECommerce()
        }
    }

    override suspend fun syncCredentialOf(): ResultType<CredentialOf> {
        return makeCallNetwork {
            suspendCancellableCoroutine { continuation ->
                remoteConfig.fetchAndActivate().addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        val credentialOf = remoteConfig.getString(CREDENTIAL_OF)
                        repositoryScope.launch {
                            preferences.edit {
                                it[CREDENTIAL_KEY] = credentialOf
                            }
                        }
                        remoteConfig.setDefaultsAsync(mapOf(CREDENTIAL_OF to credentialOf))
                        cachedCredentials = gsonFactory.fromJson(credentialOf, CredentialOfDto::class.java).mapToDomain()
                        continuation.resume(cachedCredentials!!)
                    }else{
                        continuation.cancel(task.exception)
                    }
                }.addOnFailureListener {exception ->
                    continuation.cancel(exception)
                }
            }
        }
    }

    override fun getCredentials(): CredentialOf {
        return cachedCredentials ?: run {
            val credentialJson = remoteConfig.getString(CREDENTIAL_OF)
            if (credentialJson.isNotEmpty()) {
                cachedCredentials = gsonFactory.fromJson(credentialJson, CredentialOfDto::class.java).mapToDomain()
            }
            cachedCredentials ?: CredentialOf(apikey = "41eb78998d444dbaa4922c410ef14057", url = "https://dev-api.haulmer.com/v2/dte/")
        }
    }

    override fun observeCredentialOf(): Flow<CredentialOf> = preferences
        .data.transform { preferences ->
            val json = preferences[CREDENTIAL_KEY]
            if (!json.isNullOrEmpty()) {
                val credentials = gsonFactory.fromJson(json, CredentialOfDto::class.java).mapToDomain()
                cachedCredentials = credentials
                emit(credentials)
            }
        }

    override suspend fun syncECommerce(): ResultType<ECommerce> {
        return makeCallNetwork {
            suspendCancellableCoroutine { continuation ->
                remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val ecommerceJson = remoteConfig.getString(ECOMMERCE_KEY)
                        repositoryScope.launch {
                            preferences.edit {
                                it[ECOMMERCE_KEY_PREFERENCE] = ecommerceJson
                            }
                        }
                        val ecommerce = gsonFactory.fromJson(ecommerceJson, ECommerceDto::class.java).mapToDomain()

                        continuation.resume(ecommerce)
                    } else {
                        continuation.cancel(task.exception)
                    }
                }
            }
        }
    }


    override fun observeECommerce(): Flow<ECommerce> = preferences
        .data.map { preferences ->
            val json = preferences[ECOMMERCE_KEY_PREFERENCE]
            val domain = gsonFactory.fromJson(json, ECommerceDto::class.java).mapToDomain()
            domain
        }

}