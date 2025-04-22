package com.blipblipcode.distribuidoraayl.ui.navigationGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.blipblipcode.distribuidoraayl.ui.customer.add.AddCustomerScreen
import com.blipblipcode.distribuidoraayl.ui.customer.detail.CustomerDetailScreen
import com.blipblipcode.distribuidoraayl.ui.customer.list.CustomerClientsScreen
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.CustomerScreen
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.ProductScreen
import com.blipblipcode.distribuidoraayl.ui.products.add.AddProductScreen
import com.blipblipcode.distribuidoraayl.ui.products.details.ProductDetailScreen
import com.blipblipcode.distribuidoraayl.ui.products.list.ProductListScreen

@Composable
fun HomeNavigationHost(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = ProductScreen.List){
        composable<CustomerScreen.List>{
            CustomerClientsScreen {
                navHostController.navigate(it)
            }
        }
        composable<CustomerScreen.Add>{
            AddCustomerScreen {
                navHostController.popBackStack()
            }
        }
        composable<CustomerScreen.Detail>{
            CustomerDetailScreen {
                navHostController.popBackStack()
            }
        }

        composable<ProductScreen.List>{
            ProductListScreen{
                navHostController.navigate(it)
            }
        }
        composable<ProductScreen.Add>{
            AddProductScreen {
                navHostController.popBackStack()
            }
        }
        composable<ProductScreen.Detail>{
            ProductDetailScreen {
                navHostController.popBackStack()
            }
        }
    }
}