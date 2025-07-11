package com.blipblipcode.distribuidoraayl.ui.products.add

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.ui.products.components.AddCategoryDialog
import com.blipblipcode.distribuidoraayl.ui.products.components.BarCodeSKUTextField
import com.blipblipcode.distribuidoraayl.ui.products.components.DescriptionTextField
import com.blipblipcode.distribuidoraayl.ui.products.components.OfferTextField
import com.blipblipcode.distribuidoraayl.ui.products.components.PriceTextField
import com.blipblipcode.distribuidoraayl.ui.products.components.ProductNameTextField
import com.blipblipcode.distribuidoraayl.ui.widgets.buttons.ButtonRedAyL
import com.blipblipcode.distribuidoraayl.ui.widgets.choices.FieldChoice
import com.blipblipcode.distribuidoraayl.ui.widgets.choices.OptionsSelector
import com.blipblipcode.distribuidoraayl.ui.widgets.input.getString
import com.blipblipcode.distribuidoraayl.ui.widgets.input.stringException
import com.blipblipcode.distribuidoraayl.ui.widgets.loading.LoadingScreen
import com.blipblipcode.distribuidoraayl.ui.widgets.snackbar.NotificationSnackbar
import com.blipblipcode.distribuidoraayl.ui.widgets.topBat.AddProductTopBar
import com.blipblipcode.scanner.ui.utilities.ACTION_BARCODE_SCAN
import com.blipblipcode.scanner.ui.utilities.EXTRA_BARCODE
import com.blipblipcode.scanner.ui.utilities.EXTRA_ERROR
import kotlinx.coroutines.launch

@Composable
fun AddProductScreen(
    viewModel: AddProductViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {

    val product by viewModel.product.collectAsState()
    val isValid by product.rememberIsValid()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorException by viewModel.errorException.collectAsState()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    var openCategory by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    DisposableEffect(Unit) {
        viewModel.onStarted()
        onDispose {
            viewModel.onDisposed()
        }
    }
    LaunchedEffect(errorException){
        errorException?.let {
            snackbarHostState.showSnackbar(message = context.getString(it))
        }
    }

    Scaffold(
        topBar = {
            AddProductTopBar(onBackPressed)
        },
        snackbarHost = {
            NotificationSnackbar(snackbarHostState, onDismiss = {
                viewModel.onError(null)
            })
        },
        floatingActionButton = {
            AnimatedVisibility(isValid && !isLoading) {
                FloatingActionButton(
                    onClick = {
                        viewModel.onSaveProduct(product){
                            onBackPressed.invoke()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = stringResource(R.string.add_product)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            val scanBarcode =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { onResult ->
                    if (onResult.resultCode == RESULT_OK) {
                        onResult.data?.also {
                            val barcode = it.getStringExtra(EXTRA_BARCODE)
                            viewModel.onChangedBarcode(barcode.orEmpty())
                        }
                    } else {
                        onResult.data?.also {
                            val error = it.getStringExtra(EXTRA_ERROR)
                            viewModel.onError(error)
                        }
                    }

                }
            val scanSkuBar =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { onResult ->
                    if (onResult.resultCode == RESULT_OK) {
                        onResult.data?.also {
                            val barcode = it.getStringExtra(EXTRA_BARCODE)
                            viewModel.onChangedSKU(barcode.orEmpty())
                        }
                    } else {
                        onResult.data?.also {
                            val error = it.getStringExtra(EXTRA_ERROR)
                            viewModel.onError(error)
                        }
                    }

                }

            val scope = rememberCoroutineScope()

            Column(
                Modifier
                    .padding(16.dp)
                    .imePadding()
                    .verticalScroll(rememberScrollState())
            ) {

                val udm by viewModel.udm.collectAsState()
                val sku by viewModel.sku.collectAsState()
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BarCodeSKUTextField(
                        value = sku,
                        label = R.string.sku,
                        modifier = Modifier.weight(0.6f),
                        isError = product.sku.isError,
                        errorMessage = stringException(product.sku.errorException),
                        onClickScanner = {
                            scope.launch {
                                scanSkuBar.launch(Intent(ACTION_BARCODE_SCAN))
                            }
                        }) { value ->
                        viewModel.onChangedSKU(value)
                    }
                    OptionsSelector(initial = product.udm.value?.name.orEmpty(),
                        isError = product.udm.isError,
                        errorText = stringException(product.udm.errorException),
                        choices = udm.map {
                            FieldChoice(it, it.name)
                        },
                        modifier = Modifier.weight(0.6f),
                        label = {
                            Text(text = stringResource(R.string.udm))
                        }, onValueChange = {
                            viewModel.onChangedUdm(it.data)
                        })
                }
                val barcode by viewModel.barcode.collectAsState()
                BarCodeSKUTextField(
                    value = barcode,
                    label = R.string.barcode,
                    modifier = Modifier.fillMaxWidth(),
                    isError = product.barCode.isError,
                    errorMessage = stringException(product.barCode.errorException),
                    onClickScanner = {
                        scope.launch {
                            scanBarcode.launch(Intent(ACTION_BARCODE_SCAN))
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                ) { value ->
                    viewModel.onChangedBarcode(value)
                }
                val productBrands by viewModel.productBrands.collectAsState()
                val brand by remember {
                    derivedStateOf {
                        productBrands.find { it.uid == product.brandId.value }
                    }
                }
                OptionsSelector(
                    initial = brand?.name.orEmpty(),
                    choices = productBrands.map {
                        FieldChoice(it, it.name)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = product.brandId.isError,
                    errorText = stringException(product.brandId.errorException),
                    label = {
                        Text(text = stringResource(R.string.brand))
                    }, onValueChange = {
                        viewModel.onChangedBrands(it.data)
                    })



                ProductNameTextField(product.name, modifier = Modifier.fillMaxWidth()) {
                    viewModel.onChangedName(it)
                }
                DescriptionTextField(
                    product.description,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    viewModel.onChangedDescription(it)
                }


                val categories by viewModel.categories.collectAsState()
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OptionsSelector(initial = product.category.value?.name.orEmpty(),
                        choices = categories.map {
                            FieldChoice(it, it.name)
                        },
                        isError = product.category.isError,
                        errorText = stringException(product.category.errorException),
                        modifier = Modifier.weight(0.85f),
                        label = {
                            Text(text = stringResource(R.string.categories))
                        },
                        onValueChange = {
                            viewModel.onChangedCategory(it.data)
                        })
                    ButtonRedAyL(
                        onClick = {
                            openCategory = true
                        },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .weight(0.15f)
                            .height(58.dp)
                            .offset(y = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Category")
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OfferTextField(
                        value = product.offer.value.percentage.toString(),
                        isOffer = product.offer.value.isActive,
                        isError = product.offer.isError,
                        modifier = Modifier.weight(0.85f),
                        errorException = product.offer.errorException,
                        onOfferChange = {
                            viewModel.onChangedOffer(it)
                        }
                    ) {
                        viewModel.onChangedOfferValue(it)
                    }
                    PriceTextField(
                        value = product.grossPrice.value.toString(),
                        isError = product.grossPrice.isError,
                        errorException = product.grossPrice.errorException,
                        modifier = Modifier.weight(0.85f),
                        labelText = R.string.gross_price
                    ) {
                        viewModel.onChangedGrossPrice(it)
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PriceTextField(
                        value = product.netPrice.toString(),
                        isError = false,
                        isReadOnly = true,
                        modifier = Modifier.weight(0.85f),
                        labelText = R.string.net_price
                    ) {
                        viewModel.onChangedGrossPrice(it)
                    }
                    PriceTextField(
                        value = product.total.toString(),
                        isError = false,
                        isReadOnly = true,
                        modifier = Modifier.weight(0.85f),
                        labelText = R.string.total
                    ) {
                        viewModel.onChangedGrossPrice(it)
                    }
                }
                Spacer(modifier = Modifier.height(56.dp))

            }

            AddCategoryDialog(show = openCategory, onDismiss = {
                openCategory = false
            }) {
                viewModel.onAddCategory(it)
            }

            LoadingScreen(isLoading, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.generating_document))
            }

        }
    }
}