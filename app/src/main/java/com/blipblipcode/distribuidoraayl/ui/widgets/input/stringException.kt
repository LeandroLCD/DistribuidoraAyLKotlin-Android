package com.blipblipcode.distribuidoraayl.ui.widgets.input

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.throwable.BackendErrorException
import com.blipblipcode.distribuidoraayl.domain.throwable.BarcodeOrSkuAlreadyExistsException
import com.blipblipcode.distribuidoraayl.domain.throwable.CustomerAlreadyExistsException
import com.blipblipcode.distribuidoraayl.domain.throwable.EmailIsNotValidException
import com.blipblipcode.distribuidoraayl.domain.throwable.NetworkException
import com.blipblipcode.distribuidoraayl.domain.throwable.PasswordIsNotValidException
import com.blipblipcode.distribuidoraayl.domain.throwable.RequiredFieldException
import com.blipblipcode.distribuidoraayl.domain.throwable.UnAuthenticationException
import com.blipblipcode.distribuidoraayl.domain.throwable.UserDeletedException
import com.blipblipcode.distribuidoraayl.domain.throwable.UserDisabledException
import com.blipblipcode.library.throwable.InvalidFormatException
import retrofit2.HttpException
import java.net.SocketTimeoutException

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
        is SocketTimeoutException->{
            this.getString(R.string.network_error)
        }
        is CustomerAlreadyExistsException->{
            this.getString(R.string.customer_already_exists)
        }
        is InvalidFormatException ->{
            this.getString(R.string.invalid_format)
        }
        is RequiredFieldException ->{
            this.getString(R.string.required_field)
        }
        is BarcodeOrSkuAlreadyExistsException ->{
            this.getString(R.string.barcode_or_sku_already_exists)
        }
        else -> {
            if (e != null) {
                throw e
            }else
                ""
        }
    }
}