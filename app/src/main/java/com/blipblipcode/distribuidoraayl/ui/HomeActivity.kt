package com.blipblipcode.distribuidoraayl.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.data.repositiry.customer.CustomerRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IOpenFacturaRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IProductsRepository
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.HomeNavigationHost
import com.blipblipcode.distribuidoraayl.ui.products.add.AddProductScreen
import com.blipblipcode.distribuidoraayl.ui.reports.Report
import com.blipblipcode.distribuidoraayl.ui.theme.DistribuidoraAyLTheme
import com.blipblipcode.distribuidoraayl.ui.utils.FileManager
import com.blipblipcode.distribuidoraayl.ui.utils.readJsonFile
import com.blipblipcode.library.DateTime
import com.blipblipcode.scanner.ui.utilities.EXTRA_BARCODE
import com.blipblipcode.scanner.ui.utilities.EXTRA_BARCODE_TYPE
import com.blipblipcode.scanner.ui.utilities.EXTRA_ERROR
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @Inject
    lateinit var repository: IOpenFacturaRepository

    @Inject
    lateinit var customerRepository: CustomerRepository

    @Inject
    lateinit var productsRepository: IProductsRepository

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()

            DistribuidoraAyLTheme {
                HomeNavigationHost(navHostController)
            }
        }
    }
}

