package com.blipblipcode.distribuidoraayl.ui.auth.verifiedAccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IReloadAccountUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.ISignOutUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IVerifiedEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val reloadUseCase: IReloadAccountUseCase,
    private val signOutUseCase: ISignOutUseCase,
    private val verifyAccountUseCase: IVerifiedEmailUseCase
):ViewModel() {

    fun onReload(onLoginComplete: () -> Unit, onError: suspend (Throwable) -> Unit){
        viewModelScope.launch {
            reloadUseCase().onSuccess {
                if(it.isEmailVerified){
                    onLoginComplete.invoke()
                }
            }.onError {
                onError.invoke(it)
            }
        }
    }

    fun onSignOut(){
        signOutUseCase.invoke()
    }

    fun onVerifiedAccount(onSendEmail: suspend () -> Unit,  onError: suspend  (Throwable) -> Unit) {
        viewModelScope.launch {
            verifyAccountUseCase.invoke().onSuccess {
                onSendEmail.invoke()
            }.onError{
                onError.invoke(it)
            }
        }
    }

}