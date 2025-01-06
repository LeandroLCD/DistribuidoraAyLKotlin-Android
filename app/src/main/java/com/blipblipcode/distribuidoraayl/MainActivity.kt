package com.blipblipcode.distribuidoraayl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.useCase.auth.IAuthRepository
import com.blipblipcode.distribuidoraayl.ui.theme.DistribuidoraAyLKotlinTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: IAuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            DistribuidoraAyLKotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color.Blue)){
                        LaunchedEffect(Unit) {
                           /* authRepository.createUserWithEmailAndPassword(
                                email = "blipblipcode@gmail.com",
                                password = "Aaa123"
                            ).onSuccess {
                                println(it)
                            }*/
                            authRepository.signInWithEmailAndPassword(
                                email = "blipblipcode@gmail.com",
                                password = "Aaa123"
                            ).onSuccess {user->
                                if(!user.isEmailVerified){
                                    authRepository.sendEmailVerification().onSuccess {
                                        println(it)
                                    }.onError {
                                        println(it)
                                    }
                                }
                            }.onError {
                                println(it)
                            }



                        }
                    }
                }
            }
        }
    }
}

