package com.blipblipcode.distribuidoraayl.ui.widgets.input

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.throwable.EmailIsNotValidException
import com.blipblipcode.distribuidoraayl.domain.throwable.PasswordIsNotValidException

@SuppressLint("ComposableNaming")
@Composable
fun stringException(e:Throwable?): String {
    val content = LocalContext.current
    return when(e){
        is EmailIsNotValidException -> {
            content.getString(R.string.email_is_not_valid)
        }
        is PasswordIsNotValidException -> {
            content.getString(R.string.password_is_not_valid)
        }
        else -> {
            if (e != null) {
                throw e
            }else
                ""
        }

    }
}