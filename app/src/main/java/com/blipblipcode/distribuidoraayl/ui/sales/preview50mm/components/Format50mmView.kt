package com.blipblipcode.distribuidoraayl.ui.sales.preview50mm.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocumentElectronic
import com.blipblipcode.distribuidoraayl.domain.models.sales.DteType
import com.blipblipcode.distribuidoraayl.domain.models.sales.Payment
import com.blipblipcode.distribuidoraayl.ui.utils.rememberStateBase64toBitmap
import com.blipblipcode.distribuidoraayl.ui.utils.toCurrencyFormat
import java.util.Locale

@Composable
fun Format50mmView(doc: DocumentElectronic, modifier: Modifier = Modifier){
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(Color.White),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(border = BorderStroke(1.dp, Color.Red)) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(R.string.r_u_t, doc.sale.eCommerce.rut),
                            fontWeight = FontWeight.SemiBold
                        )
                        val type = when (doc.docType) {
                            DteType.ORDER_NOTE -> R.string.order_not
                            DteType.INVOICE -> R.string.factura
                            DteType.CREDIT_NOTE -> R.string.credit_note
                        }

                        Text(stringResource(type), fontWeight = FontWeight.SemiBold)

                        Text("N° ${doc.number}", fontWeight = FontWeight.SemiBold)

                        Spacer(Modifier.height(4.dp))

                    }
                }
                if(doc.docType != DteType.ORDER_NOTE){
                    Text(
                        "S.I.I ${doc.sale.eCommerce.communeOrigin}",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        item {
            Column {
                Text(
                    doc.sale.eCommerce.companyName,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
                Text(
                    doc.sale.eCommerce.businessLine,
                    maxLines = 2,
                    overflow = TextOverflow.Clip,
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
                Text(
                    doc.sale.eCommerce.addressOrigin,
                    maxLines = 2,
                    overflow = TextOverflow.Clip,
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
                if (doc.sale.eCommerce.phone.isNotEmpty()) {
                    Text(
                        doc.sale.eCommerce.phone,
                        maxLines = 2,
                        overflow = TextOverflow.Clip,
                        fontSize = 14.sp,
                        lineHeight = 16.sp
                    )
                }
                HorizontalDivider()

            }
        }

        item {
            Column {
                Text(
                    stringResource(R.string.emission_date, doc.sale.date.format("yyyy-MM-dd")),
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
                Text(
                    stringResource(R.string.r_u_t, doc.sale.receiver.rut),
                    maxLines = 2,
                    overflow = TextOverflow.Clip,
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
                Text(
                    stringResource(R.string.receiver_name, doc.sale.receiver.name),
                    maxLines = 2,
                    overflow = TextOverflow.Clip,
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
                doc.sale.receiver.turn?.let {
                    Text(
                        stringResource(R.string.turn, it),
                        maxLines = 2,
                        overflow = TextOverflow.Clip,
                        fontSize = 14.sp,
                        lineHeight = 16.sp
                    )
                }
                Text(
                    stringResource(R.string.address_receiver, doc.sale.receiver.address),
                    maxLines = 2,
                    overflow = TextOverflow.Clip,
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
                HorizontalDivider()

            }
        }

        if (doc.payment != null) {
            item {
                val pay = when (doc.payment) {
                    Payment.Cash -> {
                        stringResource(R.string.cash).uppercase(Locale.getDefault())
                    }

                    Payment.Credit -> stringResource(R.string.credit).uppercase(Locale.getDefault())
                }
                Text(
                    stringResource(R.string.payment_receiver, pay),
                    maxLines = 2,
                    overflow = TextOverflow.Clip,
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
                HorizontalDivider()
            }
        }

        stickyHeader {
            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(R.string.article),
                        maxLines = 1,
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        stringResource(R.string.value),
                        maxLines = 1,
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                HorizontalDivider()
            }
        }

        items(doc.sale.items, key ={it.index} ){
            Text(
                "${it.sku}-${it.name}",
                maxLines = 1,
                overflow = TextOverflow.Clip,
                fontSize = 14.sp,
                lineHeight = 16.sp
            )
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    "${it.quantity} X ${it.price}",
                    maxLines = 1,
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
                Text(
                    (it.price * it.quantity).toDouble().toCurrencyFormat(),
                    maxLines = 1,
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
            }
        }

        item {
            HorizontalDivider()
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.net_amount),
                    maxLines = 1,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    doc.sale.totals.netAmount.toDouble().toCurrencyFormat(),
                    maxLines = 1,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.iva, doc.sale.totals.tax),
                    maxLines = 1,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    doc.sale.totals.taxAmount.toDouble().toCurrencyFormat(),
                    maxLines = 1,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.total_amount),
                    maxLines = 1,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    doc.sale.totals.total.toDouble().toCurrencyFormat(),
                    maxLines = 1,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.total_periodic),
                    maxLines = 1,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    doc.sale.totals.periodicAmount.toDouble().toCurrencyFormat(),
                    maxLines = 1,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.value_to_pay),
                    maxLines = 1,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    doc.sale.totals.total.toDouble().toCurrencyFormat(),
                    maxLines = 1,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        item{
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
                val timbreBitmap = rememberStateBase64toBitmap(doc.timbre.orEmpty())
                timbreBitmap?.let {
                    Image(
                        bitmap = it,
                        contentDescription = "Timbre",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        stringResource(R.string.timbre_electronic),
                        maxLines = 1,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                    )
                    Text(
                        "RES ${doc.resolution}",
                        maxLines = 1,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                    )
                    Text(
                        stringResource(R.string.verify_sii),
                        maxLines = 1,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                    )
                }
            }
        }

    }  
}