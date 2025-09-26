package com.blipblipcode.distribuidoraayl.ui.dawer

import android.content.Intent
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.blipblipcode.distribuidoraayl.MainActivity
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.ISignOutUseCase
import com.blipblipcode.distribuidoraayl.ui.dawer.models.ItemDrawer
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.AuthScreen
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.CustomerScreen
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.ProductScreen
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.ReportScreen
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.Screen
import kotlinx.coroutines.launch

@Composable
fun DistribuidoraAyLDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    onSignOutUseCase: ISignOutUseCase,
    content: @Composable () -> Unit
){
    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
                modifier = Modifier.systemBarsPadding().fillMaxWidth(0.8f)
            ) {
                DrawerContent(navController, onItemClick = {screen->
                    scope.launch {
                        navController.navigate(screen) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        drawerState.close()
                    }
                }){
                    scope.launch {
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        onSignOutUseCase.invoke()
                        activity?.startActivity(intent)
                    }
                }
            }
        }
    ) {
        content()
    }

}
@Composable
private fun DrawerContent(
    navController: NavHostController,
    onItemClick: (Screen) -> Unit,
    onLogout: () -> Unit
) {
    val currentBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(null)

    val items = remember {
        listOf(
            ItemDrawer.VectorIcon(
                route = CustomerScreen.List,
                R.string.drawer_text_customers,
                Icons.Filled.ContactPage
            ),
            ItemDrawer.DrawableIcon(
                route = ProductScreen.List,
                R.string.drawer_text_products,
                R.drawable.ic_package
            ),
            ItemDrawer.VectorIcon(
                route = ReportScreen.List,
                R.string.drawer_text_reports,
                Icons.Default.AssignmentLate
            )
        )
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        val (imgRef, optionRefs, logoutRefs) = createRefs()

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(180.dp).constrainAs(imgRef){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }

        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth()
            )
        }
        LazyColumn(Modifier.constrainAs(optionRefs) {
            top.linkTo(imgRef.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(logoutRefs.top)
            height = Dimension.fillToConstraints
        }) {
           items(items) { item ->
               val name by remember {
                   derivedStateOf {
                       item.route::class.java.canonicalName
                   }
               }

               DrawerItem(
                   item = item,
                   selected = currentBackStackEntry?.destination?.route?.contains(name.orEmpty()) == true,
                   onItemClick = {
                       onItemClick.invoke(item.route)
                   }
               )

           }
        }
        Box( modifier = Modifier.constrainAs(logoutRefs){
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }){
            DrawerItem(
                item = ItemDrawer.VectorIcon(route = AuthScreen.Login, R.string.drawer_text_logout,
                    Icons.AutoMirrored.Filled.Logout
                ),
                selected = false,
                onItemClick = {
                    onLogout()
                }
            )
        }


    }
}


@Composable
private fun DrawerItem(
    item: ItemDrawer,
    selected: Boolean,
    onItemClick: () -> Unit
) {

    NavigationDrawerItem(
        label = {
            Text(text = stringResource(id = item.name)) //, color = if(selected) Color.White else Color.Black)
        },
        selected = selected,
        onClick = {
            onItemClick()
        },
        icon = {
            when(item){
                is ItemDrawer.VectorIcon -> {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(id = item.name)
                    )
                }
                is ItemDrawer.DrawableIcon -> {
                    Icon(
                        painter = painterResource(id = item.drawable),
                        contentDescription =  stringResource(id = item.name)
                    )
                }
            }
        },
        shape = RoundedCornerShape(2.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), //TODO definir color del drawer
            unselectedContainerColor = Color.Transparent,
            selectedTextColor = Color.White,
            unselectedTextColor = Color.Black,
            selectedIconColor = Color.White,
            unselectedIconColor = Color.Black

        )
    )


}