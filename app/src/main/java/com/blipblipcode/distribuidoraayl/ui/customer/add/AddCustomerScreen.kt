package com.blipblipcode.distribuidoraayl.ui.customer.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.ui.customer.components.AddressTextField
import com.blipblipcode.distribuidoraayl.ui.customer.components.BirthDateTextField
import com.blipblipcode.distribuidoraayl.ui.customer.components.CompanyTextField
import com.blipblipcode.distribuidoraayl.ui.customer.components.ItemHolderActivity
import com.blipblipcode.distribuidoraayl.ui.customer.components.ItemHolderBranch
import com.blipblipcode.distribuidoraayl.ui.customer.components.PhoneTextField
import com.blipblipcode.distribuidoraayl.ui.customer.components.RutTextField
import com.blipblipcode.distribuidoraayl.ui.utils.removeAccents
import com.blipblipcode.distribuidoraayl.ui.utils.toFormattedString
import com.blipblipcode.distribuidoraayl.ui.widgets.carousel.CarouselView
import com.blipblipcode.distribuidoraayl.ui.widgets.choices.FieldChoice
import com.blipblipcode.distribuidoraayl.ui.widgets.choices.OptionsSelector
import com.blipblipcode.distribuidoraayl.ui.widgets.input.getString
import com.blipblipcode.distribuidoraayl.ui.widgets.loading.LoadingScreen
import com.blipblipcode.distribuidoraayl.ui.widgets.snackbar.NotificationSnackbar
import com.dsc.form_builder.ChoiceState
import com.dsc.form_builder.FormState
import com.dsc.form_builder.Validators
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomerScreen(
    viewModel: AddCustomerViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val context = LocalContext.current

    val isLoading by viewModel.isLoading.collectAsState()

    val errorException by viewModel.errorException.collectAsState()

    val customer by viewModel.customer.collectAsState()

    val formsState by remember {
        derivedStateOf {
            FormState(
                listOf(
                    ChoiceState(
                        "region", customer.regionId,
                        validators = listOf(
                            Validators.Required(context.getString(R.string.error_required))
                        ),
                    ),
                    ChoiceState(
                        "rubro", customer.rubro.description, validators = listOf(
                            Validators.Required(context.getString(R.string.error_required))
                        )
                    ),
                    ChoiceState(
                        "commune",
                        customer.commune,
                        validators = listOf(Validators.Required(context.getString(R.string.error_required)))
                    ),
                    ChoiceState(
                        "route", customer.routeId.orEmpty(), validators = listOf(
                            Validators.Required(context.getString(R.string.error_required))
                        )
                    )
                )
            )
        }
    }

    LaunchedEffect(errorException) {
        errorException?.let {
            snackbarHostState.showSnackbar(context.getString(it))
        }
    }
    Box {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.add_customer),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            onBackPressed.invoke()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            snackbarHost = {
                NotificationSnackbar(snackbarHostState)
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        if (formsState.validate()) {
                            viewModel.onSavedCustomer(customer) {
                                onBackPressed.invoke()
                            }
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ) {
                    Icon(Icons.Filled.Save, contentDescription = null)
                }

            },
            modifier = Modifier.imePadding()
        ) { innerPadding ->
            val statePicker = rememberDatePickerState()
            var isOpened by remember {
                mutableStateOf(false)
            }
            val rut by viewModel.rut.collectAsState()

            Column(
                Modifier
                    .padding(innerPadding)
                    .padding(vertical = 16.dp, horizontal = 12.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                /*rut*/
                Spacer(modifier = Modifier.width(26.dp))
                RutTextField(rut, onRutChanged = {
                    viewModel.onRutChanged(it)
                },
                    modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = {
                        viewModel.onFindRut(rut.value)
                    }) {
                        Icon(Icons.Filled.Sync, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.width(26.dp))
                /*name*/
                CompanyTextField(value = customer.companyName) {
                    viewModel.onNameChanged(it)
                }


                val scrollState = rememberScrollState()
                Column(Modifier.verticalScroll(scrollState)) {
                    val rubros by viewModel.rubros.collectAsState(initial = listOf())

                    val routes by viewModel.routes.collectAsState(initial = listOf())

                    val routesCustomer by remember {
                        derivedStateOf {
                            routes.find { it.uid == customer.routeId }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val rubroState = formsState.getState<ChoiceState>("rubro")
                        OptionsSelector(
                            rubroState.value,
                            modifier = Modifier.weight(0.6f),
                            choices = rubros,
                            isError = rubroState.hasError,
                            errorText = rubroState.errorMessage,
                            label = {
                                Text(
                                    stringResource(R.string.category),
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }) {
                            viewModel.onRubrosChanged(it.data)
                            rubroState.change(it.data.description)
                        }
                        val routeState = formsState.getState<ChoiceState>("route")
                        OptionsSelector(
                            routesCustomer?.name.orEmpty(),
                            modifier = Modifier.weight(0.4f),
                            isError = routeState.hasError,
                            errorText = routeState.errorMessage,
                            choices = routes.map { FieldChoice(it, it.name) },
                            label = {
                                Text(stringResource(R.string.routes))
                            }) {
                            viewModel.onRouteChanged(it.data)
                            routeState.change(it.data.name)
                        }

                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val birthDate by viewModel.birthDate.collectAsState()
                        BirthDateTextField(
                            birthDate = birthDate,
                            onBirthdayChanged = { date ->
                                viewModel.onBirthdayChanged(date)
                            },
                            modifier = Modifier.weight(0.6f)
                        ) {
                            IconButton(onClick = {
                                isOpened = true
                            }) {
                                Icon(Icons.Filled.CalendarMonth, contentDescription = null)
                            }
                        }



                        PhoneTextField(
                            value = customer.phone,
                            modifier = Modifier.weight(0.4f)
                        ) {
                            viewModel.onPhoneChanged(it)
                        }

                    }


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        val regions by viewModel.regions.collectAsState(listOf())
                        val regionsCustomer by remember(customer) {
                            derivedStateOf {
                                regions.find { it.id == customer.regionId }
                            }
                        }
                        val regionState = formsState.getState<ChoiceState>("region")
                        OptionsSelector(
                            regionsCustomer?.name.orEmpty(),
                            modifier = Modifier.weight(0.5f),
                            isError = regionState.hasError,
                            errorText = regionState.errorMessage,
                            choices = regions.map { FieldChoice(it, it.name) },
                            label = {
                                Text(stringResource(R.string.region))
                            }) {
                            viewModel.onRegionChanged(it.data)
                            regionState.change(it.data.name)

                        }

                        val communes by viewModel.communes.collectAsState()
                        val customerState = formsState.getState<ChoiceState>("commune")
                        OptionsSelector(
                            customer.commune.removeAccents(),
                            modifier = Modifier.weight(0.5f),
                            isError = customerState.hasError,
                            errorText = customerState.errorMessage,
                            choices = communes.map { FieldChoice(it, it.name) },
                            label = {
                                Text(stringResource(R.string.commune))
                            }) {
                            viewModel.onCommuneChanged(it.data)
                            customerState.change(it.data.name)
                        }
                    }

                    AddressTextField(
                        value = customer.address,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.onAddressChanged(it)
                    }


                    customer.branches?.let { branches ->
                        CarouselView(items = branches, label = stringResource(R.string.branches)) {
                            ItemHolderBranch(it, modifier = Modifier.fillMaxWidth()) { b, c ->
                                viewModel.onChangedBranch(b, c)
                            }
                        }
                    }

                    customer.activities?.let { activities ->
                        CarouselView(
                            items = activities,
                            label = stringResource(R.string.activities)
                        ) {
                            ItemHolderActivity(it)
                        }
                    }
                }

                AnimatedVisibility(isOpened) {
                    DatePickerDialog(
                        onDismissRequest = { isOpened = false },
                        properties = DialogProperties(usePlatformDefaultWidth = true),
                        confirmButton = {
                            Button(
                                onClick = {
                                    isOpened = false
                                    statePicker.selectedDateMillis?.let {
                                        viewModel.onBirthdayChanged(Date(it).toFormattedString())
                                    }

                                }
                            ) {
                                Text(text = stringResource(R.string.ok))
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { isOpened = false }
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

            }
        }
        LoadingScreen(
            isLoading,
            Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f))
        ) {
            Text(stringResource(R.string.generating_document))
        }

    }
}