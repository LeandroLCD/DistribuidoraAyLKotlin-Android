package com.blipblipcode.distribuidoraayl.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.blipblipcode.distribuidoraayl.data.repositiry.customer.CustomerRepository
import com.blipblipcode.distribuidoraayl.data.repositiry.preferences.SystemPreferencesRepository
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.customer.Route
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IOpenFacturaRepository
import com.blipblipcode.distribuidoraayl.ui.customer.add.AddCustomerViewModel
import com.blipblipcode.distribuidoraayl.ui.theme.DistribuidoraAyLTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @Inject
    lateinit var repository: IOpenFacturaRepository

    @Inject
    lateinit var customerRepository: CustomerRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
    val viewModel = hiltViewModel<AddCustomerViewModel>()
            DistribuidoraAyLTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val customer by viewModel.customer.collectAsStateWithLifecycle()
                    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
                    val errorException by viewModel.errorException.collectAsStateWithLifecycle()
                        LaunchedEffect(customer.rut){
                            Log.d("homeActivity", "customer: $customer")
                        }
                    LaunchedEffect(errorException){
                        errorException?.let {
                            Log.d("homeActivity", "error: $it")
                        }
                    }
                    LaunchedEffect(isLoading){
                        Log.d("homeActivity", "loading: $isLoading")
                    }
                    LaunchedEffect(Unit){
                      /*repository.getTaxpayer("10764166-2").onSuccess {
                          println(it)
                      }.onError {
                          println(it)
                      }*/
                       /* customerRepository.setRegions().onSuccess {
                            println("Exito")
                        }.onError {
                            println(it)
                        }*/

                       /* customerRepository.setRubros().onSuccess {
                            println(it)
                        }.onError {
                            println(it)
                        }*/

                    }

                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding).background(Color.Blue)){

                        Greeting(
                            name = "Android",
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}