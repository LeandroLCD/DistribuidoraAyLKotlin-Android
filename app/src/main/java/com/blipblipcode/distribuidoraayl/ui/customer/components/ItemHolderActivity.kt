package com.blipblipcode.distribuidoraayl.ui.customer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.models.customer.Activity

@Composable
fun ItemHolderActivity(activity: Activity, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier, shape = RoundedCornerShape(8.dp),
        shadowElevation = 8.dp,
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = activity.name)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = activity.code.toString())
                val text = if (activity.isMainActivity) stringResource(R.string.yes) else stringResource(
                    R.string.not
                )
                Text(
                    text = stringResource(
                        R.string.is_main_activity,
                        text
                    )
                )
            }
        }
    }
}