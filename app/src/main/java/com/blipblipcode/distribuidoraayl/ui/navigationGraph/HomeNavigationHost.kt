package com.blipblipcode.distribuidoraayl.ui.navigationGraph

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.ISignOutUseCase
import com.blipblipcode.distribuidoraayl.ui.customer.add.AddCustomerScreen
import com.blipblipcode.distribuidoraayl.ui.customer.detail.CustomerDetailScreen
import com.blipblipcode.distribuidoraayl.ui.customer.list.CustomerClientsScreen
import com.blipblipcode.distribuidoraayl.ui.dawer.DistribuidoraAyLDrawer
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.CustomerScreen
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.ProductScreen
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.ReportScreen
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.SalesScreen
import com.blipblipcode.distribuidoraayl.ui.products.add.AddProductScreen
import com.blipblipcode.distribuidoraayl.ui.products.details.ProductDetailScreen
import com.blipblipcode.distribuidoraayl.ui.products.list.ProductListScreen
import com.blipblipcode.distribuidoraayl.ui.report.list.ReportSaleListScreen
import com.blipblipcode.distribuidoraayl.ui.sales.SaleScreen
import kotlinx.coroutines.launch

@Composable
fun HomeNavigationHost(navHostController: NavHostController, onISignOutUseCase: ISignOutUseCase) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavHost(navController = navHostController, startDestination = SalesScreen) {

        composable<SalesScreen> {
            DistribuidoraAyLDrawer(navHostController, drawerState, onISignOutUseCase) {
                SaleScreen {
                    scope.launch {
                        drawerState.open()
                    }
                }
            }
        }

        composable<CustomerScreen.List> {
            DistribuidoraAyLDrawer(navHostController, drawerState, onISignOutUseCase) {
                CustomerClientsScreen(drawerOpen = {
                    scope.launch {
                        drawerState.open()
                    }
                }) {
                    navHostController.navigate(it)
                }
            }
        }
        composable<CustomerScreen.Add> {
            AddCustomerScreen {
                navHostController.popBackStack()
            }
        }
        composable<CustomerScreen.Detail> {
            CustomerDetailScreen {
                navHostController.popBackStack()
            }
        }

        composable<ProductScreen.List> {
            DistribuidoraAyLDrawer(navHostController, drawerState, onISignOutUseCase) {
                ProductListScreen(drawerOpen = {
                    scope.launch {
                        drawerState.open()
                    }
                }) {
                    navHostController.navigate(it)
                }
            }
        }
        composable<ProductScreen.Add> {
            AddProductScreen {
                navHostController.popBackStack()
            }
        }
        composable<ProductScreen.Detail> {
            ProductDetailScreen {
                navHostController.popBackStack()
            }
        }

        composable<ReportScreen.List> {
            DistribuidoraAyLDrawer(navHostController, drawerState, onISignOutUseCase) {
                ReportSaleListScreen(drawerOpen = {
                    scope.launch {
                        drawerState.open()
                    }
                }) {
                    navHostController.navigate(it)
                }
            }
        }

    }
}