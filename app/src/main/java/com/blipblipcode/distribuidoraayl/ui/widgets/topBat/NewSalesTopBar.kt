package com.blipblipcode.distribuidoraayl.ui.widgets.topBat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.library.DateTime
import com.blipblipcode.library.model.FormatType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSalesTopBar(date:DateTime, onClickMenu: () -> Unit, onClickDate: () -> Unit) {
    TopAppBar(title = { Text(stringResource(R.string.drawer_text_sales)) },
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
                Text(date.format(FormatType.Short('-')), Modifier.clickable {
                    onClickDate.invoke()
                })
        })
}