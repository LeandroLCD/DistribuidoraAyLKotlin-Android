package com.blipblipcode.distribuidoraayl.ui.auth.verifiedAccount

import android.os.CountDownTimer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.AuthScreen
import com.blipblipcode.distribuidoraayl.ui.widgets.buttons.ButtonRedAyL
import com.blipblipcode.distribuidoraayl.ui.widgets.input.getString
import com.blipblipcode.distribuidoraayl.ui.widgets.snackbar.NotificationSnackbar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    loginComplete: () -> Unit,
    navigateTo: (AuthScreen) -> Unit
) {
    val context = LocalContext.current
    val notificationHost = remember {
        SnackbarHostState()
    }
    var isEnabledBtnVerified by remember { mutableStateOf(true) }
    val timerVerified by remember {
        mutableStateOf(
            object : CountDownTimer(20_000, 100) {
                override fun onTick(millisUntilFinished: Long) {
                    isEnabledBtnVerified = false
                }

                override fun onFinish() {
                    isEnabledBtnVerified = true
                }
            }
        )
    }

    var isEnabledBtnSendEmail by remember { mutableStateOf(true) }
    val timerSendEmail by remember {
        mutableStateOf(
            object : CountDownTimer(120_000, 100) {
                override fun onTick(millisUntilFinished: Long) {
                    isEnabledBtnSendEmail = false
                }

                override fun onFinish() {
                    isEnabledBtnSendEmail = true
                }
            }
        )
    }
    Scaffold(topBar = {
        TopAppBar(title = {},
            colors = TopAppBarDefaults.topAppBarColors(actionIconContentColor = MaterialTheme.colorScheme.primary),
            actions = {
                IconButton(onClick = {
                    viewModel.onSignOut()
                    navigateTo(AuthScreen.Login)
                }) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, null)
                }
            })

    },
        snackbarHost = {
            NotificationSnackbar(notificationHost)
        }) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.account))

            LottieAnimation(
                composition = composition, iterations = LottieConstants.IterateForever
            )
            Spacer(Modifier.width(18.dp))
            Text(
                text = stringResource(R.string.verified_account_msj),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.weight(1f))
            Row(
                Modifier.height(60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonRedAyL(
                    enabled = isEnabledBtnVerified,
                    onClick = {
                        timerVerified.start()
                        viewModel.onReload(onLoginComplete = {
                            loginComplete.invoke()
                        }){
                            notificationHost.showSnackbar(context.getString(it))
                        }
                    },
                    contentPadding = PaddingValues(16.dp, 8.dp),
                    modifier = Modifier.width(120.dp)
                ) {
                    Text(text = stringResource(R.string.verified_account))
                }
                Spacer(Modifier.width(8.dp))
                ButtonRedAyL(
                    enabled = isEnabledBtnSendEmail,
                    onClick = {
                        timerSendEmail.start()
                        viewModel.onVerifiedAccount(onSendEmail = {
                            notificationHost.showSnackbar(context.getString(R.string.email_sent))
                        }){
                            notificationHost.showSnackbar(context.getString(it))
                        }

                    },
                    contentPadding = PaddingValues(16.dp, 8.dp),
                    modifier = Modifier.width(120.dp)
                ) {
                    Text(text = stringResource(R.string.send_email))
                }
            }
        }
    }
}