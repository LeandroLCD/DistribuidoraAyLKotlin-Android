package com.blipblipcode.distribuidoraayl.ui.widgets.input

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.throwable.BackendErrorException
import com.blipblipcode.distribuidoraayl.domain.throwable.EmailIsNotValidException
import com.blipblipcode.distribuidoraayl.domain.throwable.NetworkException
import com.blipblipcode.distribuidoraayl.domain.throwable.PasswordIsNotValidException
import com.blipblipcode.distribuidoraayl.domain.throwable.UnAuthenticationException
import com.blipblipcode.distribuidoraayl.domain.throwable.UserDeletedException
import com.blipblipcode.distribuidoraayl.domain.throwable.UserDisabledException

@SuppressLint("ComposableNaming")
@Composable
fun stringException(e:Throwable?): String {
    val context = LocalContext.current
    return context.getString(e)
}

fun Context.getString(e:Throwable?):String{
    return when(e){
        is EmailIsNotValidException -> {
            this.getString(R.string.email_is_not_valid)
        }
        is PasswordIsNotValidException -> {
            this.getString(R.string.password_is_not_valid)
        }
        is UnAuthenticationException ->{
            this.getString(R.string.not_authentication)
        }
        is UserDisabledException -> getString(R.string.user_disabled)
        is UserDeletedException -> getString(R.string.user_deleted)
        is NetworkException ->{
            this.getString(R.string.network_error)
        }
        is BackendErrorException ->{
            e.message.orEmpty()
        }
        else -> {
            if (e != null) {
                throw e
            }else
                ""
        }
    }
}