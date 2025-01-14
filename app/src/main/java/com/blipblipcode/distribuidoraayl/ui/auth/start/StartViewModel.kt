package com.blipblipcode.distribuidoraayl.ui.auth.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.throwable.UnAuthenticationException
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IGetUserUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IReloadAccountUseCase
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StartViewModel @Inject constructor(
    private val reloadAccountUseCase: IReloadAccountUseCase
) : ViewModel() {

    private val _errorException = MutableStateFlow<Throwable?>(null)
    val errorException = _errorException.asStateFlow()

    fun getUser(onCompleteLogin: () -> Unit, navigateTo: (Screen) -> Unit) {
        viewModelScope.launch {
            reloadAccountUseCase.invoke().onSuccess { user ->
                if (user.isEmailVerified) {
                    onCompleteLogin.invoke()
                } else {
                    navigateTo.invoke(Screen.VerifiedAccount)
                }
            }.onError {
                if (it is UnAuthenticationException) {
                    navigateTo.invoke(Screen.Login)
                } else {
                    _errorException.tryEmit(it)
                }
            }
        }
    }

}