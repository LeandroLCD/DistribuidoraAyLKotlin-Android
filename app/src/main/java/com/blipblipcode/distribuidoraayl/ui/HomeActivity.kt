package com.blipblipcode.distribuidoraayl.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.customer.Route
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
import com.blipblipcode.distribuidoraayl.ui.theme.DistribuidoraAyLTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @Inject
    lateinit var repository: ICustomerRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DistribuidoraAyLTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    LaunchedEffect(Unit){
                      val result =  repository.createRoute(Route(name = "Ruta 2")).onSuccess {
                            println("Se creo la ruta")
                        }.onError { exception ->
                            println("No se creo la ruta ${exception.message}")
                        }

                       when(result){
                            is ResultType.Error -> {
                                println("Error  ${result.exception.message}")
                            }
                            is ResultType.Success -> {
                                println("Exito")
                            }
                        }

                        repository.getRoutes().collect{routes->
                            routes.forEach {
                                println("${it.uid} - ${it.name}")
                            }
                        }


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