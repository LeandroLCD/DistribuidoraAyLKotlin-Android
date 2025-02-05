package com.blipblipcode.distribuidoraayl.ui.customer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.models.customer.Branch

@Composable
fun ItemHolderBranch(
    branch: Branch,
    isReadOnly: Boolean = false,
    modifier: Modifier = Modifier,
    onChanged: (Branch, String) -> Unit
) {
    Surface(
        modifier = modifier, shape = RoundedCornerShape(8.dp),
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(0.2f),
                value = branch.sapCode.orEmpty(),
                singleLine = true,
                readOnly = isReadOnly,
                textStyle = LocalTextStyle.current.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                onValueChange = {
                    onChanged(branch, it)
                },
                label = { Text(text = stringResource(R.string.sac)) }
            )
            Column(
                Modifier
                    .weight(0.8f)
            ) {
                Text(
                    text = branch.address,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = branch.commune,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}