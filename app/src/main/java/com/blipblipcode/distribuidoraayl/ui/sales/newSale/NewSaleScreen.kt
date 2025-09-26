package com.blipblipcode.distribuidoraayl.ui.sales.newSale

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.blipblipcode.distribuidoraayl.ui.products.components.BarCodeSKUTextField
import com.blipblipcode.distribuidoraayl.ui.sales.SaleViewModel
import com.blipblipcode.distribuidoraayl.ui.sales.models.ProductQuantity
import com.blipblipcode.distribuidoraayl.ui.widgets.choices.FieldChoice
import com.blipblipcode.distribuidoraayl.ui.widgets.choices.OptionsSelector
import com.blipblipcode.distribuidoraayl.ui.widgets.dialog.CustomerListDialog
import com.blipblipcode.distribuidoraayl.ui.widgets.dialog.ProductListDialog
import com.blipblipcode.distribuidoraayl.ui.widgets.topBat.NewSalesTopBar
import com.blipblipcode.library.DateTime
import com.blipblipcode.scanner.ui.utilities.ACTION_BARCODE_SCAN
import com.blipblipcode.scanner.ui.utilities.EXTRA_BARCODE
import com.blipblipcode.scanner.ui.utilities.EXTRA_ERROR
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSaleScreen(
    viewModel: SaleViewModel,
    openDrawer: () -> Unit
) {

    val date by viewModel.date.collectAsState()
    var isVisibleDatePick by remember {
        mutableStateOf(false)
    }

    var showCustomerDialog by remember {
        mutableStateOf(false)
    }
    var showProducts by remember {
        mutableStateOf(false)
    }
    val isLetter by viewModel.isLetter.collectAsState()

    val scanBarcode =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { onResult ->
            if (onResult.resultCode == RESULT_OK) {
                onResult.data?.also {
                    val barcode = it.getStringExtra(EXTRA_BARCODE)
                    viewModel.onSearch(barcode.orEmpty())
                }
            } else {
                onResult.data?.also {
                    val error = it.getStringExtra(EXTRA_ERROR)
                    viewModel.onError(error)
                }
            }

        }

    Scaffold(
        topBar = {
            NewSalesTopBar(
                date,
                isLetter = isLetter, onClickMenu = {
                    openDrawer.invoke()
                },
                onDocumentChanged = {
                    viewModel.onLetterChanged(it)
                }) {
                isVisibleDatePick = true
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {

            val customer by viewModel.customerSelected.collectAsState()
            val activitySelected by viewModel.activitySelected.collectAsState()
            val branchSelected by viewModel.branchSelected.collectAsState()
            Surface(
                Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color.LightGray),
                shadowElevation = 8.dp,
                shape = RoundedCornerShape(6.dp)
            ) {
                var showActivities by remember {
                    mutableStateOf(false)
                }

                Column {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            if (customer != null) {
                                IconButton(onClick = {
                                    showActivities = !showActivities
                                }) {
                                    Icon(
                                        if (!showActivities)
                                            Icons.AutoMirrored.Filled.ArrowRight
                                        else
                                            Icons.Default.ArrowDropDown,
                                        contentDescription = null
                                    )
                                }
                            }
                            TextButton(
                                contentPadding = PaddingValues(4.dp),
                                shape = RectangleShape,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    showCustomerDialog = true

                                }) {
                                Box(
                                    contentAlignment = Alignment.CenterStart,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    AnimatedContent(customer != null) {
                                        if (it) {
                                            Text(
                                                text = customer!!.companyName,
                                                overflow = TextOverflow.Ellipsis,
                                                maxLines = 1,
                                                fontSize = 16.sp
                                            )
                                        } else {
                                            Text(
                                                text = stringResource(R.string.select_customer),
                                                fontSize = 16.sp,
                                                modifier = Modifier.padding(start = 40.dp)
                                            )
                                        }
                                    }
                                }

                            }
                        }
                    }
                    AnimatedVisibility(showActivities && customer != null) {
                        var isClosed by remember {
                            mutableStateOf(Triple(false, false, true))
                        }
                        LaunchedEffect(isClosed) {
                            if (isClosed.first == true && isClosed.second == true) {
                                delay(1500L)
                                isClosed = Triple(false, false, true)
                                showActivities = false
                            }
                        }
                        Column(Modifier.padding(horizontal = 8.dp)) {
                            customer?.activities?.let {
                                OptionsSelector(
                                    initial = it.find { it.code == activitySelected?.code }?.turn.orEmpty(),
                                    modifier = Modifier.fillMaxWidth(),
                                    choices = it.mapIndexed { index, it ->
                                        FieldChoice(index, it.turn)
                                    },
                                    label = {
                                        Text(text = stringResource(R.string.activities))
                                    }
                                ) { op ->
                                    viewModel.onActivitySelected(it[op.data])
                                    isClosed = isClosed.copy(
                                        first = true
                                    )
                                }

                            }

                            customer?.branches?.let {
                                OptionsSelector(
                                    initial = it.find { it.code == branchSelected?.code }?.address.orEmpty(),
                                    modifier = Modifier.fillMaxWidth(),
                                    choices = it.mapIndexed { index, it ->
                                        FieldChoice(index, it.address)
                                    },
                                    label = {
                                        Text(text = stringResource(R.string.address))
                                    }
                                ) { op ->
                                    viewModel.onBranchSelected(it[op.data])
                                    isClosed = isClosed.copy(
                                        second = true
                                    )
                                }

                            }
                        }
                    }
                }
            }

            val scope = rememberCoroutineScope()
            val search by viewModel.search.collectAsState()
            val productsFound by viewModel.productsFount.collectAsState()

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                SearchProducts(
                    search = search,
                    onSearch = {
                        viewModel.onSearch(it)
                    },
                    onClickScanner = {
                        scope.launch {
                            val intent = Intent(ACTION_BARCODE_SCAN)
                            scanBarcode.launch(intent)
                        }
                    },
                    onSelected = {
                        viewModel.onAddProduct(it)
                    },
                    trailingIcon = {
                        Surface(
                            shape = FloatingActionButtonDefaults.smallShape,
                            modifier = Modifier
                                .size(46.dp)
                                .padding(4.dp),
                            onClick = {
                                showProducts = !showProducts
                            },
                        ) {
                            Image(
                                painter = painterResource(R.drawable.img_frozen),
                                contentDescription = null,
                                modifier = Modifier.padding(2.dp)
                            )
                        }
                    },
                    productsFound = productsFound
                )
            }

            val productsInCard by viewModel.productsInCard.collectAsState()

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(top = 4.dp, bottom = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productsInCard) {
                    ProductCard(it) {
                        viewModel.onDeleteProduct(it.product.uid)
                    }
                }
            }

            Surface(
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 2.dp,
                shape = RoundedCornerShape(6.dp)
            ) {

                val eCommerce by viewModel.ecommerce.collectAsState()


                val totals by viewModel.totals.collectAsState()

                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Row {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Subtotal:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(text = "IVA:", fontSize = 12.sp, fontWeight = FontWeight.Bold)

                            Text(
                                text = "Total:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )

                        }
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = " $ ${totals.netAmount}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "% ${eCommerce?.iva ?: 0.0}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = " $ ${totals.total}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )

                        }

                    }

                    Surface(
                        enabled = customer != null && eCommerce != null,
                        onClick = {
                            if (customer != null && eCommerce != null) {

                                viewModel.onSale(
                                    customer = customer!!,
                                    activity = activitySelected,
                                    branch = branchSelected,
                                    date = date,
                                    eCommerce = eCommerce!!,
                                    isLetter = isLetter,
                                    total = totals
                                )
                            }
                        },
                        shape = FloatingActionButtonDefaults.smallShape,
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterEnd),
                        color = MaterialTheme.colorScheme.primary,
                        shadowElevation = 8.dp
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.Send,
                            contentDescription = null,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }

        }


        AnimatedVisibility(showCustomerDialog) {
            val route by viewModel.routeSelected.collectAsState()
            val routes by viewModel.routes.collectAsState()

            val customers by viewModel.customers.collectAsState()
            val search by viewModel.searchCustomer.collectAsState()
            CustomerListDialog(
                customers = customers,
                routes = routes,
                routeSelected = route,
                onRouteSelected = {
                    viewModel.onRouteSelected(it)
                },
                onDismiss = {
                    showCustomerDialog = false
                },
                onCustomerSelected = {
                    viewModel.onCustomerSelected(it)
                },
                search = search,
                onSearch = {
                    viewModel.onSearchCustomer(it)
                }
            )
        }

        AnimatedVisibility(isVisibleDatePick) {
            val statePicker = rememberDatePickerState()
            DatePickerDialog(
                modifier = Modifier.padding(horizontal = 20.dp),
                onDismissRequest = { isVisibleDatePick = false },
                properties = DialogProperties(usePlatformDefaultWidth = true),
                confirmButton = {
                    Button(
                        onClick = {
                            isVisibleDatePick = false
                            statePicker.selectedDateMillis?.let {
                                viewModel.onDateChanged(DateTime.fromMillis(it, "UTC"))
                            }

                        }
                    ) {
                        Text(text = stringResource(R.string.ok))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { isVisibleDatePick = false }
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(
                    state = statePicker,
                    showModeToggle = true
                )
            }
        }

        val products by viewModel.catalogue.collectAsState()
        val categorySelected by viewModel.categorySelected.collectAsState()
        val categories by viewModel.categories.collectAsState()
        val search by viewModel.searchProduct.collectAsState()

        if (showProducts) {
            ProductListDialog(
                products = products,
                categories = categories,
                categorySelected = categorySelected,
                onCategorySelected = {
                    viewModel.onCategorySelected(it)
                },
                search = search,
                onSearch = {
                    viewModel.onSearchProduct(it)
                },
                onDismiss = {
                    showProducts = false
                    viewModel.clearProductSelected()
                },
                onProductSelected = {
                    viewModel.onProductSelected(it.product)
                }) {
                viewModel.onAddProducts(it)
            }
        }
    }
}

@Composable
fun ProductCard(product: ProductQuantity, onDelete: () -> Unit) {
    Surface(shadowElevation = 8.dp, shape = RoundedCornerShape(8.dp)) {
        ConstraintLayout(
            Modifier
                .fillMaxWidth()
                .requiredHeight(70.dp)
        ) {
            val (box, offer, name, delete, total) = createRefs()

            val lineVertical = createGuidelineFromStart(50.dp)
            val lineHorizontal = createGuidelineFromTop(0.5f)
            val keyboardController = LocalSoftwareKeyboardController.current
            var value by remember {
                mutableStateOf(product.quantity.toString())
            }
            LaunchedEffect(product.quantity) {
                value = product.quantity.toString()
            }
            val focusRequester = LocalFocusManager.current
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .border(
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .constrainAs(box) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(lineVertical)
                    },
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    modifier = Modifier
                        .width(32.dp)

                        .onFocusChanged {
                            if (!it.isFocused) {
                                val new = value.filter { it.isDigit() }.toIntOrNull() ?: 1
                                value = new.toString()
                                product.onChangedQuantity(new)
                            }
                        },
                    maxLines = 1,
                    value = value,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        val new = value.filter { it.isDigit() }.toIntOrNull() ?: 1
                        value = new.toString()
                        product.onChangedQuantity(new)
                        keyboardController?.hide()
                        focusRequester.clearFocus()
                    }),
                    onValueChange = {
                        value = it.filter { it.isDigit() }.take(2)
                    }
                )
            }
            Text(
                text = product.product.name, modifier = Modifier
                    .constrainAs(name) {
                        start.linkTo(lineVertical)
                        end.linkTo(delete.start)
                        top.linkTo(parent.top)
                        if (product.product.offer.percentage > 0f) {
                            bottom.linkTo(lineHorizontal)
                        } else {
                            bottom.linkTo(parent.bottom)
                        }
                        width = Dimension.fillToConstraints
                    }
                    .padding(start = 12.dp),
                textAlign = TextAlign.Start
            )
            IconButton(onClick = {
                onDelete.invoke()
            }, modifier = Modifier.constrainAs(delete) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }) {
                Icon(Icons.Default.DeleteOutline, null)
            }
            if (product.product.offer.percentage > 0f) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.constrainAs(offer) {
                        start.linkTo(lineVertical)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(total.start)
                        width = Dimension.fillToConstraints
                    }) {
                    Checkbox(checked = product.isOffer, onCheckedChange = {
                        product.onChangedOffer(it)
                    })
                    Text(
                        text = stringResource(
                            R.string.offer_percentage,
                            product.product.offer.percentage
                        ), fontSize = 12.sp
                    )
                }

            }

            Text(
                text = "$ ${product.totalPrice.value}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(8.dp)
                    .constrainAs(total) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
    }
}

@Composable
fun SearchProducts(
    search: String,
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    onClickScanner: () -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    onSelected: (Product) -> Unit,
    productsFound: List<Product>
) {

    val context = LocalContext.current
    var rowSize by remember {
        mutableStateOf(Size.Zero)
    }
    val errorMessage by remember(productsFound, search) {
        derivedStateOf {
            if (productsFound.isEmpty() && search.isNotBlank()) {
                context.getString(R.string.product_not_found)
            } else {
                null
            }
        }
    }
    Column(modifier = modifier) {

        BarCodeSKUTextField(
            value = search,
            label = R.string.search,
            trailingIcon = trailingIcon,
            modifier = modifier
                .fillMaxWidth()
                .onGloballyPositioned { layoutCoordinates ->
                    rowSize = layoutCoordinates.size.toSize()
                },
            isError = errorMessage != null,
            errorMessage = errorMessage,
            onClickScanner = {
                onClickScanner.invoke()
            }
        ) {
            onSearch(it)
        }
        if (productsFound.isNotEmpty()) {
            with(LocalDensity.current) {
                Popup(
                    alignment = Alignment.TopStart,
                    offset =
                        IntOffset(0, rowSize.height.minus(16.dp.toPx()).roundToInt()),
                    onDismissRequest = { },
                ) {
                    Surface(
                        modifier = Modifier
                            .width(rowSize.width.toDp())
                            .heightIn(max = 250.dp),
                        shape = RoundedCornerShape(4.dp),
                        shadowElevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            productsFound.forEach { product ->
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        onSelected(product)
                                        onSearch("")
                                    },
                                    color = Color.Transparent
                                ) {
                                    Text(
                                        text = product.name,
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 12.dp
                                        ),
                                        style = MaterialTheme.typography.bodyLarge,
                                        softWrap = false,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }

    }
}

