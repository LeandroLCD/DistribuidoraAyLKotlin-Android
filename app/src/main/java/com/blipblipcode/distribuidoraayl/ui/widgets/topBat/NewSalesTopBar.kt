package com.blipblipcode.distribuidoraayl.ui.widgets.topBat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.library.DateTime
import com.blipblipcode.library.model.FormatType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSalesTopBar(date:DateTime,
                   onClickMenu: () -> Unit,
                   onDocumentChanged: (Boolean) -> Unit,
                   onClickDate: () -> Unit) {
    var isVisible by remember {
        mutableStateOf(false)
    }

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
            Column {
                IconButton(onClick = {
                    isVisible = true
                }) {
                    Icon(Icons.Filled.MoreVert, null)
                }
                DropdownMenu(
                    expanded = isVisible,
                    onDismissRequest = { isVisible = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.format_letter)) },
                        onClick = {
                            isVisible = false
                            onDocumentChanged.invoke(false)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.format_ticket)) },
                        onClick = {
                            isVisible = false
                            onDocumentChanged.invoke(false)
                        }
                    )
                }
            }
        })
}