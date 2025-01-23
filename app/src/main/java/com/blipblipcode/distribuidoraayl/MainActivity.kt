package com.blipblipcode.distribuidoraayl

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.blipblipcode.distribuidoraayl.data.repositiry.preferences.SystemPreferencesRepository
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IAuthRepository
import com.blipblipcode.distribuidoraayl.ui.HomeActivity
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.StartNavigationHost
import com.blipblipcode.distribuidoraayl.ui.theme.DistribuidoraAyLTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: IAuthRepository

    @Inject
    lateinit var systemPreferences: SystemPreferencesRepository

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaunchedEffect(Unit){
                systemPreferences.syncCredentialOf().onSuccess {
                    println(it)
                }.onError {
                    println(it)
                }
            }
            DisposableEffect(Unit) {
                val originalOrientation = requestedOrientation
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                onDispose {
                    requestedOrientation = originalOrientation
                }
            }

            val navHostController = rememberNavController()

            DistribuidoraAyLTheme {
                StartNavigationHost(navHostController) {
                    val intent = Intent(applicationContext, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }

            }
        }
    }
}

