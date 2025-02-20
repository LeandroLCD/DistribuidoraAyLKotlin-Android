package com.blipblipcode.distribuidoraayl.ui.navigationGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.blipblipcode.distribuidoraayl.ui.customer.add.AddCustomerScreen
import com.blipblipcode.distribuidoraayl.ui.customer.detail.CustomerDetailScreen
import com.blipblipcode.distribuidoraayl.ui.customer.list.CustomerClientsScreen
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.CustomerScreen

@Composable
fun HomeNavigationHost(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = CustomerScreen.List){
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
    }
}