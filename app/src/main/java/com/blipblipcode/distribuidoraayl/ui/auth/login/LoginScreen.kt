package com.blipblipcode.distribuidoraayl.ui.auth.login

import android.os.CountDownTimer
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.ui.theme.primaryColor
import com.blipblipcode.distribuidoraayl.ui.widgets.input.EmailTextField
import com.blipblipcode.distribuidoraayl.ui.widgets.input.PasswordTextField
import com.blipblipcode.distribuidoraayl.ui.widgets.input.stringException

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    Scaffold() { innerPadding ->
        var recoveryPassword by remember {
            mutableStateOf(false)
        }
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            val (cardRefs, iconRefs) = createRefs()
            val iconLineTop = createGuidelineFromTop(0.15f)
            val cardLineTop = createGuidelineFromTop(0.2f)
            val iconLineBottom = createGuidelineFromTop(0.25f)

            Surface(Modifier.constrainAs(cardRefs) {
                top.linkTo(cardLineTop)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }.padding(horizontal = 16.dp),
                shadowElevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, primaryColor)
                ) {
                AnimatedContent(recoveryPassword, label = "recoveryPassword") {
                    if (it) {

                        RecoveryPassword(viewModel) {
                            recoveryPassword = false
                        }
                    } else {
                        Login(viewModel, modifier = Modifier) {
                            recoveryPassword = true
                        }
                    }
                }


            }
            Box(Modifier.constrainAs(iconRefs) {
                top.linkTo(iconLineTop)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(iconLineBottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints

            }, contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.ic_logo),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null
                )
            }
        }

    }
}

@Composable
fun Login(viewModel: LoginViewModel, modifier: Modifier.Companion, action: () -> Unit) {
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    var isVisiblePassword by remember {
        mutableStateOf(false)
    }
    val enableLogin by remember {
        derivedStateOf {
            email.value.isNotEmpty() && password.value.isNotEmpty() && !email.isError && !password.isError

        }
    }
    Column(
        modifier
            .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(34.dp))
        Text(
            text = stringResource(R.string.sing_in),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,

            )
        Spacer(Modifier.height(24.dp))
        EmailTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email.value,
            onChangedValue = {
                viewModel.onEmailChanged(it)
            },
            isError = email.isError,
            errorMessage = stringException(email.errorException)
        )
        Spacer(Modifier.height(16.dp))

        PasswordTextField (
            modifier = Modifier.fillMaxWidth(),
            value = password.value,
            onChangedValue = {
                viewModel.onPasswordChanged(it)
            },
            isError = password.isError,
            errorMessage = stringException(password.errorException),
            isVisiblePassword = isVisiblePassword,
            onChangedPasswordVisibility = {
                isVisiblePassword = !isVisiblePassword
            }
        )
        TextButton(
            onClick = {
                action.invoke()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = stringResource(R.string.forgot_password), color = Color.Blue)
        }
        Spacer(Modifier.height(34.dp))

        Button(
            enabled = enableLogin,
            onClick = {
                    viewModel.onLogin(email.value, password.value)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.sing_in))
                Spacer(Modifier.width(8.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward, null
                )
            }
        }

    }

}

@Composable
fun RecoveryPassword(viewModel: LoginViewModel, onChangedContent: () -> Unit) {
    val email by viewModel.email.collectAsStateWithLifecycle()
    var isEnabled by remember { mutableStateOf(true) }
    val timer by remember {
        mutableStateOf(
            object : CountDownTimer(10000, 100) {
                override fun onTick(millisUntilFinished: Long) {
                    isEnabled = false
                }

                override fun onFinish() {
                    isEnabled = true
                }
            }
        )
    }

    Column(
        Modifier
            .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(34.dp))
        Text(
            text = stringResource(R.string.recovery_password),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,

            )
        Spacer(Modifier.height(24.dp))
        EmailTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email.value,
            isError = email.isError,
            errorMessage = stringException(email.errorException),
            onChangedValue = {
                viewModel.onEmailChanged(it)
            })

        Spacer(Modifier.height(8.dp))

        TextButton(
            shape = MaterialTheme.shapes.small,
            onClick = {
                onChangedContent.invoke()
            }
        ) {
            Text(text = stringResource(R.string.sing_in), color = Color.Blue)
        }
        Spacer(Modifier.height(34.dp))

        Button(
            onClick = {
                timer.start()
            },
            enabled = isEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.send_email))
                Spacer(Modifier.width(8.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward, null
                )
            }
        }

    }
}
