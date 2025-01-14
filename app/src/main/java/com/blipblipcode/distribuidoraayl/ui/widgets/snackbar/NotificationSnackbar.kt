package com.blipblipcode.distribuidoraayl.ui.widgets.snackbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.ui.widgets.buttons.ButtonRedAyL

@Composable
fun NotificationSnackbar(
    state: SnackbarHostState,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {

    SnackbarHost(hostState = state, modifier = modifier){data->
        Surface(
            shadowElevation = 8.dp,
            shape = CircleShape,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            contentColor = MaterialTheme.colorScheme.inverseOnSurface
        ) {
            Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(text = data.visuals.message, fontSize = 20.sp)
                Spacer(Modifier.width(8.dp))
                data.visuals.actionLabel?.let {
                    ButtonRedAyL(onClick = {
                        onRetry?.invoke()
                        data.dismiss()
                    }) {
                        Text(text = it)
                    }
                }
            }
        }
    }
}

