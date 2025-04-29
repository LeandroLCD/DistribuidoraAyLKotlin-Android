package com.blipblipcode.distribuidoraayl.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.ISignOutUseCase
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.HomeNavigationHost
import com.blipblipcode.distribuidoraayl.ui.theme.DistribuidoraAyLTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @Inject
    lateinit var onISignOutUseCase: ISignOutUseCase

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()

            DistribuidoraAyLTheme {
                HomeNavigationHost(navHostController, onISignOutUseCase)
            }
        }
    }
}

