package com.blipblipcode.distribuidoraayl.ui.products.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.models.products.Udm
import com.blipblipcode.distribuidoraayl.ui.auth.login.models.DataField
import com.blipblipcode.distribuidoraayl.ui.products.components.BarCodeSKUTextField
import com.blipblipcode.distribuidoraayl.ui.products.components.DescriptionTextField
import com.blipblipcode.distribuidoraayl.ui.products.components.ProductNameTextField
import com.blipblipcode.distribuidoraayl.ui.widgets.buttons.ButtonRedAyL
import com.blipblipcode.distribuidoraayl.ui.widgets.choices.FieldChoice
import com.blipblipcode.distribuidoraayl.ui.widgets.choices.OptionsSelector

@Composable
fun AddProductScreen(
    viewModel: AddProductViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {

    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(Modifier.padding(16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BarCodeSKUTextField(
                        value = "1234567890123",
                        label = R.string.barcode,
                        onClickScanner = {

                        }){value->
                        viewModel.onChangedBarcode(value)
                    }

                    BarCodeSKUTextField(
                        value = "1234567",
                        label = R.string.sku,
                        onClickScanner = {

                        }){value->
                        viewModel.onChangedSKU(value)
                    }
                }
                ProductNameTextField(DataField("")){

                }
                DescriptionTextField(DataField("")){

                }
                val udm by viewModel.udm.collectAsState()
                val productBrands by viewModel.productBrands.collectAsState()
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                    OptionsSelector(initial = "", choices = udm.map {
                        FieldChoice(it, it.name)
                    }, label = {
                        Text(text = stringResource(R.string.udm))
                    }, onValueChange = {
                        viewModel.onChangedUdm(it.data)
                    })
                    OptionsSelector(initial = "", choices = productBrands.map {
                        FieldChoice(it, it.name)
                    }, label = {
                        Text(text = stringResource(R.string.categories))
                    }, onValueChange = {
                        viewModel.onChangedBrands(it.data)
                    })

                }

                val categories by viewModel.categories.collectAsState()
                Row {
                    OptionsSelector(initial = "", choices = categories.map {
                        FieldChoice(it, it.name)
                    }, label = {
                        Text(text = stringResource(R.string.categories))
                    }, onValueChange = {
                        viewModel.onChangedCategory(it.data)
                    })
                    ButtonRedAyL(onClick = {
                        //TODO open create Category
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Category")
                    }
                }


            }

        }
    }
}