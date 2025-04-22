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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.blipblipcode.distribuidoraayl.data.repositiry.customer.CustomerRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IOpenFacturaRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IProductsRepository
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.HomeNavigationHost
import com.blipblipcode.distribuidoraayl.ui.products.add.AddProductScreen
import com.blipblipcode.distribuidoraayl.ui.theme.DistribuidoraAyLTheme
import com.blipblipcode.scanner.ui.utilities.EXTRA_BARCODE
import com.blipblipcode.scanner.ui.utilities.EXTRA_BARCODE_TYPE
import com.blipblipcode.scanner.ui.utilities.EXTRA_ERROR
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
            val scope = rememberCoroutineScope()
            val barcodeScanner = remember { mutableStateOf("") }
            val scanActivity = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { onResult->
                if (onResult.resultCode == RESULT_OK) {
                    onResult.data?.also {
                        val barcode = it.getStringExtra(EXTRA_BARCODE)
                        val barcodeType = it.getStringExtra(EXTRA_BARCODE_TYPE)
                        barcodeScanner.value = "$barcode, $barcodeType"

                        Log.d("scanActivity", "scan barcode: $barcode, $barcodeType")
                    }
                }else{
                    Log.d("scanActivity","barcode error: ${onResult.data?.getStringExtra(EXTRA_ERROR)}")
                }

            }
            /*val fileManager = FileManager.with(this)
            val jsonString = readJsonFile(R.raw.abril)
            val jsonFactory = Gson()
            LaunchedEffect(Unit) {

                val type = object : TypeToken<Map<String, Report>>() {}.type
                val reportSale: Map<String, Report> = Gson().fromJson(jsonString, type)

                reportSale.apply {
                    val reporting = buildList {

                        values.map { report->
                            report.sales?.map {
                                    add(it.toString(report.invoice))
                            }
                            report.prodAyL?.map {
                                val code = it.productCode.take(2)
                                if(code != "13"){
                                    add(it.toString(report.invoice))
                                }
                            }
                        }
                    }
                    val ultValue = values.last()
                    val date = DateTime.fromString(ultValue.fecha.substring(0,10))
                    fileManager.title("reporte de ventas febrero")
                    fileManager.folder("reports/marzo")
                    fileManager.addFile("vta228352${date.format("yyyyMMdd")}.txt", reporting.joinToString(Char(10).toString()))
                    fileManager.send()
                }
            }*/

            DistribuidoraAyLTheme {
                HomeNavigationHost(navHostController)
            }
        }
    }
}

