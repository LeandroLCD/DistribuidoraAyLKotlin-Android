package com.blipblipcode.distribuidoraayl.ui.widgets.topBat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.blipblipcode.distribuidoraayl.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListTopBar(onClickMenu: () -> Unit, onClickFilter: () -> Unit) {
    TopAppBar(title = { Text(stringResource(R.string.product_list)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = Color.White,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White),
        navigationIcon = {
            IconButton(onClick = {
                onClickMenu.invoke()
            }) {
                Icon(Icons.Filled.Menu, null)
            }
        },
        actions = {
            IconButton(onClick = {
                onClickFilter.invoke()
            }) {
                Icon(Icons.Filled.FilterAlt, null)
            }
        })
}