package com.blipblipcode.distribuidoraayl.ui.widgets.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.models.sales.Payment
import com.blipblipcode.distribuidoraayl.ui.theme.DistribuidoraAyLTheme
import com.blipblipcode.distribuidoraayl.ui.theme.primaryColor
import com.blipblipcode.distribuidoraayl.ui.widgets.buttons.ButtonRedAyL
import com.blipblipcode.distribuidoraayl.ui.widgets.buttons.ToggleButton
import com.blipblipcode.distribuidoraayl.ui.widgets.buttons.ToggleOption
import com.blipblipcode.distribuidoraayl.ui.widgets.buttons.rememberToggleButtonState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentDialog(title: String, onDismiss: () -> Unit, onConfirm: (Payment) -> Unit) {

    var toggleOption by rememberToggleButtonState()
    val context = LocalContext.current
    BasicAlertDialog(
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        Surface(shape = RoundedCornerShape(24.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Row(Modifier.padding(top = 26.dp, bottom = 4.dp)) {
                    ToggleButton(
                        selectedOption = toggleOption,
                        textLeft = stringResource(R.string.cash),
                        textRight = stringResource(R.string.credit),
                        onLeftSelected = {
                            toggleOption = ToggleOption.LEFT
                        },
                        onRightSelected = {
                            toggleOption = ToggleOption.RIGHT
                        }
                    )
                }
                val string = remember(toggleOption){
                    derivedStateOf {
                        buildAnnotatedString {
                            when (toggleOption) {
                                ToggleOption.LEFT -> {
                                    append(context.getString(R.string.has_select))
                                    append(" ")
                                    withStyle(style = SpanStyle(color = primaryColor)) {
                                        append(context.getString(R.string.cash))
                                    }
                                    append(" ")
                                    append(context.getString(R.string.with_payment_method))

                                }

                                ToggleOption.RIGHT -> {
                                    append(context.getString(R.string.has_select))
                                    append(" ")
                                    withStyle(style = SpanStyle(color = primaryColor)) {
                                        append(context.getString(R.string.credit))
                                    }
                                    append(" ")
                                    append(context.getString(R.string.with_payment_method))
                                }

                                ToggleOption.NONE -> {
                                    append(context.getString(R.string.payment_method))
                                }
                            }
                        }
                    }
                }
                Text(text = string.value, fontSize = 12.sp, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
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
                    ButtonRedAyL(
                        enabled = toggleOption != ToggleOption.NONE,
                        onClick = {
                            when (toggleOption) {
                                ToggleOption.LEFT -> {
                                    onConfirm.invoke(Payment.Cash)
                                }

                                ToggleOption.RIGHT -> {
                                    onConfirm.invoke(Payment.Credit)
                                }

                                else -> {}
                            }
                            onDismiss.invoke()
                        }) {
                        Text(text = stringResource(R.string.add))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PaymentDialogPReview() {
    DistribuidoraAyLTheme {
        PaymentDialog(title = "Generar Factura", onDismiss = { }, onConfirm = {})
    }
}