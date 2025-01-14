package com.blipblipcode.distribuidoraayl.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.throwable.EmailIsNotValidException
import com.blipblipcode.distribuidoraayl.domain.throwable.PasswordIsNotValidException
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IForgotPasswordUseCase
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
class LoginViewModel @Inject constructor(
    private val loginUseCase: ILoginUseCase,
    private val forgotPasswordUseCase: IForgotPasswordUseCase
) : ViewModel() {
    /** states*/
    private val _email = MutableStateFlow(DataField("blipblipcode@gmail.com"))
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow(DataField("Aa1234"))
    val password = _password.asStateFlow()
    private val _pwdRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6,}\$".toRegex()

    private val _errorException = MutableStateFlow<Throwable?>(null)
    val errorException = _errorException.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

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
            if (value.matches(_pwdRegex)) {
                old.copy(
                    value = value,
                    isError = false,
                    errorException = null
                )
            } else {
                old.copy(
                    value = value,
                    isError = true,
                    errorException = PasswordIsNotValidException()
                )
            }

        }

    }

    fun onLogin(
        email: String,
        password: String,
        onVerifiedEmail: () -> Unit,
        onCompleteLogin: () -> Unit
    ) {
        _isLoading.update { true }
        viewModelScope.launch {
            loginUseCase(email, password).onSuccess { user ->
                if (user.isEmailVerified) {
                    onCompleteLogin.invoke()
                } else {
                    onVerifiedEmail.invoke()
                }
            }.onError { exception ->
                _errorException.update { exception }
            }
            _isLoading.update { false }
        }
    }

    fun onForgotPassword(email: String, onSuccess: () -> Unit) {
        _isLoading.update { true }
        viewModelScope.launch {
            forgotPasswordUseCase(email).onSuccess {
                onSuccess.invoke()
            }.onError {
                _errorException.tryEmit(it)
            }
            _isLoading.update { false }
        }
    }

}