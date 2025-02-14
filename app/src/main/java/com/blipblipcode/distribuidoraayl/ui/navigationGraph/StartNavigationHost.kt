package com.blipblipcode.distribuidoraayl.ui.navigationGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.blipblipcode.distribuidoraayl.ui.auth.login.LoginScreen
import com.blipblipcode.distribuidoraayl.ui.auth.start.StartScreen
import com.blipblipcode.distribuidoraayl.ui.auth.verifiedAccount.AccountScreen
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.AuthScreen

@Composable
fun StartNavigationHost(navHostController: NavHostController, onCompleteLogin: () -> Unit) {
    NavHost(navController = navHostController, startDestination = AuthScreen.Start) {
        composable<AuthScreen.Start> {
            StartScreen(onCompleteLogin = onCompleteLogin) {screen->
                navHostController.popBackStack()
                navHostController.navigate(screen)
            }
        }
        composable<AuthScreen.Login> {
            LoginScreen( onCompleteLogin = onCompleteLogin){screen->
                navHostController.popBackStack()
                navHostController.navigate(screen)
            }
        }
        composable<AuthScreen.VerifiedAccount> {
            AccountScreen(loginComplete = onCompleteLogin) {screen->
                navHostController.popBackStack()
                navHostController.navigate(screen)
            }
        }


    }
}