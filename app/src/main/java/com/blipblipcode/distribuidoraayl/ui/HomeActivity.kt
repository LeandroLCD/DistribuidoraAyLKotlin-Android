package com.blipblipcode.distribuidoraayl.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.blipblipcode.distribuidoraayl.data.repositiry.reportSale.ReportSaleRepository
import com.blipblipcode.distribuidoraayl.data.worker.SyncUpRepository
import com.blipblipcode.distribuidoraayl.data.worker.SyncUpWorker
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.ISignOutUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.worker.ISyncUpRepository
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.HomeNavigationHost
import com.blipblipcode.distribuidoraayl.ui.theme.DistribuidoraAyLTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @Inject
    lateinit var onISignOutUseCase: ISignOutUseCase

    @Inject
    lateinit var syncUpRepository: ISyncUpRepository

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        syncUpRepository.runPeriodicWork(1L)
        setContent {
            val navHostController = rememberNavController()
            LaunchedEffect(Unit) {
                syncUpRepository.workInfoFlow().collect {
                    Log.d("SyncUpWorker", "status= ${it.last().state}, workInfoFlow: ${it.last().outputData.getBoolean("success", false)}")
                }
            }
            DistribuidoraAyLTheme {
                HomeNavigationHost(navHostController, onISignOutUseCase)
            }
        }
    }
}

