package com.blipblipcode.distribuidoraayl.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.blipblipcode.distribuidoraayl.data.repositiry.customer.CustomerRepository
import com.blipblipcode.distribuidoraayl.data.repositiry.product.ProductsRepository
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.models.products.Category
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IOpenFacturaRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IProductsRepository
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.HomeNavigationHost
import com.blipblipcode.distribuidoraayl.ui.theme.DistribuidoraAyLTheme
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaunchedEffect(Unit) {
                repeat(5){ count ->
                    productsRepository.createCategory(Category(name = "Category $count")).onSuccess {
                        Log.d("productsRepository", "Category created successfully $count")
                    }.onError {
                        Log.d("productsRepository", "Category created error $count, ${it.message}")
                    }
                }


                productsRepository.getCategoryList().collect{
                    Log.d("productsRepository", "Category: $it")
                }
            }
            val navHostController = rememberNavController()
            DistribuidoraAyLTheme {
                HomeNavigationHost(navHostController)
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