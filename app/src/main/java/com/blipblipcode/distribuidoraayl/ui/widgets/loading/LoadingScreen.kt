package com.blipblipcode.distribuidoraayl.ui.widgets.loading

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.blipblipcode.distribuidoraayl.R

@Composable
fun LoadingScreen(isLoading: Boolean, modifier: Modifier = Modifier) {
    AnimatedVisibility(isLoading,
        enter = scaleIn(),
        exit = scaleOut(),
        modifier = modifier
    ) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {

            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))

            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop,
            )

            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "logo",
                modifier = Modifier.size(50.dp)
            )
        }
    }
}