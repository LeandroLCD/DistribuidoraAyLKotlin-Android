package com.blipblipcode.distribuidoraayl.ui.customer.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import com.blipblipcode.distribuidoraayl.ui.customer.models.DataFilter
import com.blipblipcode.distribuidoraayl.ui.customer.models.TypeFilter
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.CustomerScreen
import com.blipblipcode.distribuidoraayl.ui.widgets.buttons.ButtonRedAyL
import com.blipblipcode.distribuidoraayl.ui.widgets.swipe.SwipeMenuItem
import com.blipblipcode.distribuidoraayl.ui.widgets.swipe.rememberSwipeMenuState
import com.blipblipcode.distribuidoraayl.ui.widgets.topBat.CustomerClientTopBar
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun CustomerClientsScreen(
    viewModel: CustomerListViewModel = hiltViewModel(),
    onNavigateTo: (CustomerScreen) -> Unit
) {
    val customers by viewModel.customers.collectAsState()
    var isVisible by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            CustomerClientTopBar(onClickMenu = {
                /*TODO open drawer*/
            },
                onAddRoute = {
                    isVisible = true
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
                    onNavigateTo.invoke(CustomerScreen.Add)
                }) {
                Icon(Icons.Filled.Add, null)
            }
        }
    ) { innerPadding ->

        val filters by viewModel.appliedFilters.collectAsState()

        LazyColumn(
            Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            stickyHeader {
                FlowRow(maxLines = 2) {
                    filters.forEach {
                        ItemFilter(it) {
                            /*Todo Delete filter*/
                        }
                    }

                }
            }
            items(customers, key = { it.rut }) {
                ItemHolder(it,
                    modifier = Modifier.height(76.dp),
                    onEdit = { customer ->
                    onNavigateTo.invoke(CustomerScreen.Detail(customer.rut, true))
                }, onDelete = { customer ->
                    viewModel.onDeleteCustomer(customer)
                }) { customer ->
                    onNavigateTo.invoke(CustomerScreen.Detail(customer.rut))
                }
            }
        }
        AnimatedVisibility(isVisible) {
            val name = remember {
                mutableStateOf(TextFieldValue())
            }
            BasicAlertDialog(onDismissRequest = {
                isVisible = false
            }) {
                Surface {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.add_route),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(name.value,
                            onValueChange = {
                                name.value = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                                focusedTextColor = MaterialTheme.colorScheme.primary,
                                selectionColors = TextSelectionColors(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                )
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Go,
                            ),
                            keyboardActions = KeyboardActions(onGo = {

                            }),
                            shape = RoundedCornerShape(15.dp),
                            label = { Text(stringResource(R.string.name)) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ButtonRedAyL(onClick = {
                            isVisible = false
                            //Todo crear metodo para agregar ruta
                        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            Text(stringResource(R.string.accept))
                        }
                    }
                }
            }
        }
    }


}

@Composable
fun ItemFilter(dataFilter: DataFilter, onDelete: (DataFilter) -> Unit) {
    val transition = updateTransition(dataFilter.type, label = "updateTransition")
    val color = transition.animateColor(label = "color") {
        when (it) {
            TypeFilter.Boolean.Equals -> {
                Color.Green
            }

            TypeFilter.Date.Between, TypeFilter.Date.Equals, TypeFilter.Date.GreaterThan, TypeFilter.Date.LessThan -> {
                Color.Blue
            }

            TypeFilter.Number.Equals, TypeFilter.Number.GreaterThan, TypeFilter.Number.LessThan -> {
                Color.Yellow
            }

            TypeFilter.Text.Equals, TypeFilter.Text.Contains -> {
                Color.Red
            }
        }
    }
    val contentColor = transition.animateColor(label = "color") {
        when (it) {
            TypeFilter.Boolean.Equals -> {
                Color.White
            }

            TypeFilter.Date.Between, TypeFilter.Date.Equals, TypeFilter.Date.GreaterThan, TypeFilter.Date.LessThan -> {
                Color.Black
            }

            TypeFilter.Number.Equals, TypeFilter.Number.GreaterThan, TypeFilter.Number.LessThan -> {
                Color.Black
            }

            TypeFilter.Text.Equals, TypeFilter.Text.Contains -> {
                Color.White
            }
        }
    }
    Surface(
        color = color.value,
        contentColor = contentColor.value,
        onClick = {
            onDelete.invoke(dataFilter)
        },
        tonalElevation = 8.dp,
        shape = CircleShape
    ) {
        Column(
            modifier = Modifier.padding(16.dp, 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(dataFilter.toString(), fontSize = 12.sp)
        }
    }
}

@Composable
fun ItemHolder(
    customer: Customer,
    modifier: Modifier = Modifier,
    onEdit: (Customer) -> Unit,
    onDelete: (Customer) -> Unit,
    onSelected: (Customer) -> Unit
) {
    val state = rememberSwipeMenuState()
    val scope = rememberCoroutineScope()
    SwipeMenuItem(state, modifier.fillMaxWidth(), contentMenu = {
        Row {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Blue),
                onClick = {
                    onEdit.invoke(customer)
                    scope.launch {
                        state.closeMenu()
                    }
                }) {
                Icon(Icons.Filled.Edit, null)
            }
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                onClick = {
                    onDelete.invoke(customer)
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
                onSelected.invoke(customer)
            },
            shape = RoundedCornerShape(8.dp),
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = customer.companyName, fontSize = 16.sp, maxLines = 1)
                    Row(
                        Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Rut: ${customer.rut}", fontSize = 12.sp)
                        if(customer.phone.length > 7){
                            Text(text = "Tlf: ${customer.phone}", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

}
