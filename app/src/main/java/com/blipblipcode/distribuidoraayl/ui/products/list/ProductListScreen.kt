package com.blipblipcode.distribuidoraayl.ui.products.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.ProductScreen
import com.blipblipcode.distribuidoraayl.ui.widgets.input.ItemFilter
import com.blipblipcode.distribuidoraayl.ui.widgets.swipe.SwipeMenuItem
import com.blipblipcode.distribuidoraayl.ui.widgets.swipe.rememberSwipeMenuState
import com.blipblipcode.distribuidoraayl.ui.widgets.topBat.ProductListTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = hiltViewModel(),
    drawerOpen: () -> Unit,
    onNavigateTo: (ProductScreen) -> Unit
) {
    val products by viewModel.products.collectAsState()
    Scaffold(
        topBar = {
            ProductListTopBar(onClickMenu = {
                drawerOpen.invoke()
            },
                onClickFilter = {
                    /*TODO add filter*/
                })
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.Black,
                onClick = {
                    onNavigateTo.invoke(ProductScreen.Add)
                }) {
                Icon(Icons.Filled.Add, null)
            }
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            val filters by viewModel.appliedFilters.collectAsState()
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                stickyHeader {
                    FlowRow(maxLines = 2) {
                        filters.forEach {
                            ItemFilter(it) { f ->
                                viewModel.onDeleteFilter(f)
                            }
                        }
                    }
                }
                items(products.size, key = { it }) {
                    ItemProductHolder(products[it],
                        onEdit = { product ->
                            onNavigateTo.invoke(ProductScreen.Detail(uId = product.uid, true))
                        }, onDelete = { product ->
                            viewModel.onDeleteProduct(product)
                        }){product->
                        onNavigateTo.invoke(ProductScreen.Detail(uId = product.uid))
                    }
                }

            }
        }

    }
}

@Composable
fun ItemProductHolder(
    product: Product,
    modifier: Modifier = Modifier,
    onEdit: (Product) -> Unit,
    onDelete: (Product) -> Unit,
    onSelected: (Product) -> Unit
) {
    val state = rememberSwipeMenuState()
    val scope = rememberCoroutineScope()
    SwipeMenuItem(state, modifier.fillMaxWidth(), contentMenu = {
        Row {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Blue),
                onClick = {
                    onEdit.invoke(product)
                    scope.launch {
                        state.closeMenu()
                    }
                }) {
                Icon(Icons.Filled.Edit, null)
            }
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                onClick = {
                    onDelete.invoke(product)
                    scope.launch {
                        state.closeMenu()
                    }
                }) {
                Icon(Icons.Filled.Delete, null)
            }
        }
    }) {
        Surface(
            onClick = {
                scope.launch {
                    state.closeMenu()
                }
                onSelected.invoke(product)
            },
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = product.name, fontSize = 16.sp, maxLines = 1)
                    product.category?.name?.let {
                        Text(
                            text = it,
                            fontSize = 16.sp,
                            maxLines = 1
                        )
                    }
                    if (product.offer.isActive) {
                        Text(
                            text = stringResource(
                                R.string.offer_percentage,
                                product.offer.percentage
                            ), fontSize = 12.sp
                        )
                    }
                }
                Text(text = "$ ${product.grossPrice}", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

            }
        }
    }

}