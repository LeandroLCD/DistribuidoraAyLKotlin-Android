package com.blipblipcode.distribuidoraayl.ui.widgets.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.models.products.Category
import com.blipblipcode.distribuidoraayl.ui.sales.models.ProductSelected
import com.blipblipcode.distribuidoraayl.ui.widgets.buttons.ButtonRedAyL
import com.blipblipcode.distribuidoraayl.ui.widgets.input.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListDialog(
    products: List<ProductSelected>,
    categories: List<Category>,
    modifier: Modifier = Modifier,
    categorySelected: Category? = null,
    onCategorySelected: (Category?) -> Unit,
    search: String,
    onSearch: (String) -> Unit,
    onDismiss: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    onProductSelected: (ProductSelected) -> Unit,
    addProductsSelected: (List<ProductSelected>) -> Unit
) {

    BasicAlertDialog(
        onDismissRequest = {
            onDismiss.invoke()
        },
        properties = properties
    ) {
        var showRoute by remember {
            mutableStateOf(false)
        }
        Surface(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxHeight(0.7f),
            shape = RoundedCornerShape(4.dp)
        ) {
            Column(Modifier.padding(8.dp)) {
                Row(verticalAlignment = CenterVertically) {
                    SearchBar(
                        value = search,
                        label = stringResource(R.string.search),
                        modifier = Modifier.weight(1f),
                    ) {
                        onSearch.invoke(it)
                    }

                    Box {
                        IconButton(onClick = {
                            showRoute = !showRoute
                        }) {
                            Icon(Icons.Outlined.MoreVert, contentDescription = null)
                        }

                        DropdownMenu(
                            expanded = showRoute,
                            onDismissRequest = {
                                showRoute = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.all),
                                        color = if (categorySelected == null)
                                            MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                contentPadding = PaddingValues(vertical = 2.dp, horizontal = 8.dp),
                                onClick = {
                                    showRoute = false
                                    onCategorySelected.invoke(null)
                                })
                            categories.forEach { option ->
                                val color = if (categorySelected?.uid == option.uid)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = option.name,
                                            color = color
                                        )
                                    },
                                    contentPadding = PaddingValues(
                                        vertical = 2.dp,
                                        horizontal = 8.dp
                                    ),
                                    onClick = {
                                        showRoute = false
                                        onCategorySelected.invoke(option)
                                    })
                            }
                        }

                    }
                }


                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                    items(products) {item->
                        Surface(shadowElevation = 4.dp,
                            shape = RoundedCornerShape(4.dp),
                            color = if (item.isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                            modifier = Modifier.clickable{
                                onProductSelected.invoke(item)
                            }) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = item.product.name)
                                Text(text = "$ ${item.product.grossPrice}")
                            }
                        }
                    }

                }
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    ButtonRedAyL(
                        onClick = {
                            onDismiss.invoke()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    ButtonRedAyL(onClick = {
                        addProductsSelected.invoke(products.filter { it.isSelected })
                        onDismiss.invoke()
                    }) {
                        Text(text = stringResource(R.string.add))
                    }
                }
            }

        }
    }
}