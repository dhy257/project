package com.example.frontend

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.frontend.ui.theme.FrontendTheme
import kotlinx.coroutines.delay

@SuppressLint
class Loading : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FrontendTheme {
                SplashScreen()
            }
        }
    }

    @Preview
    @Composable
    private fun SplashScreen() {
        val alpha = remember {
            androidx.compose.animation.core.Animatable(0f)
        }
        LaunchedEffect(key1 = true) {
            alpha.animateTo(
                1f,
                animationSpec = tween(1500)
            )
            delay(2000)
            startActivity(Intent(this@Loading, MainActivity::class.java))
        }
        Image(
            modifier = Modifier.alpha(alpha.value),
            painter = painterResource(R.drawable.loading),
            contentDescription = null
        )
    }
}
