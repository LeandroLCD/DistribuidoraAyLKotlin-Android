package com.blipblipcode.distribuidoraayl.ui.auth.start

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.throwable.UnAuthenticationException
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.AuthScreen
import com.blipblipcode.distribuidoraayl.ui.widgets.input.getString
import com.blipblipcode.distribuidoraayl.ui.widgets.snackbar.NotificationSnackbar


@Composable
fun StartScreen(
    viewModel: StartViewModel = hiltViewModel(),
    onCompleteLogin: () -> Unit,
    navigationTo: (AuthScreen) -> Unit
) {

    val context = LocalContext.current

    val errorException by viewModel.errorException.collectAsState()

    val errorSnackbar = remember {
        SnackbarHostState()
    }

    LaunchedEffect(errorException) {
        if (errorException != null && errorException !is UnAuthenticationException) {
            errorSnackbar.showSnackbar(
                context.getString(errorException),
                context.getString(R.string.ok)
            )
        }
    }

    Scaffold(
        snackbarHost = {
            NotificationSnackbar(state = errorSnackbar, onRetry = {
                viewModel.getUser(onCompleteLogin = onCompleteLogin) {
                    navigationTo.invoke(it)
                }
            })
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center
        ) {
            val scale = remember {
                Animatable(0f)
            }
            LaunchedEffect(key1 = true) {
                scale.animateTo(
                    targetValue = 0.7f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = {
                            OvershootInterpolator(4f).getInterpolation(it)
                        }),

                    )
                viewModel.getUser(onCompleteLogin = onCompleteLogin) {
                    navigationTo.invoke(it)
                }
            }

            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale.value)
            )
        }
    }
}