package com.blipblipcode.distribuidoraayl.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.throwable.EmailIsNotValidException
import com.blipblipcode.distribuidoraayl.domain.throwable.PasswordIsNotValidException
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.ILoginUseCase
import com.blipblipcode.distribuidoraayl.ui.auth.login.models.DataField
import com.blipblipcode.distribuidoraayl.ui.utils.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: ILoginUseCase) : ViewModel() {
    /** states*/
    private val _email = MutableStateFlow(DataField(""))
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow(DataField(""))
    val password = _password.asStateFlow()
    private val _pwdRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6,}\$".toRegex()

    /**events*/

    fun onEmailChanged(value: String) {
        _email.update { old ->
            if (value.isEmailValid()) {
                old.copy(
                    value = value,
                    isError = false,
                    errorException = null
                )
            } else {
                old.copy(
                    value = value,
                    isError = true,
                    errorException = EmailIsNotValidException()
                )
            }

        }
    }

    fun onPasswordChanged(value: String) {
        _password.update { old ->
            if(value.matches(_pwdRegex)){
                old.copy(
                    value = value,
                    isError = false,
                    errorException = null
                )
            }else{
                old.copy(
                    value = value,
                    isError = true,
                    errorException = PasswordIsNotValidException()
                )
            }

        }

    }

    fun onLogin(email: String, password: String){
        viewModelScope.launch {
            loginUseCase(email, password).onSuccess {
                //navegar a otra pantalla
                println(it)
            }.onError {
                println(it)
            }
        }
    }

    fun onForgotPassword(){

    }

}