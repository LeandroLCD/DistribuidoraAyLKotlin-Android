package com.blipblipcode.distribuidoraayl.ui.widgets.topBat

import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.blipblipcode.distribuidoraayl.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailTopBar(isEditable:Boolean, onEdit:()->Unit, onBackPressed:()->Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                onBackPressed.invoke()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        },
        title = {
            AnimatedContent(isEditable) {
                if (it) {
                    Text(
                        text = stringResource(R.string.edit_customer),
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = stringResource(R.string.customer_detail),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        actions = {
            if (!isEditable) {
                IconButton(onClick = {
                    onEdit.invoke()
                }) {
                    Icon(Icons.Filled.EditNote, contentDescription = null)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}