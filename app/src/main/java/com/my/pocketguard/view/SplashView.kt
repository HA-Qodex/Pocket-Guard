package com.my.pocketguard.view

import android.app.Activity
import android.view.Window
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.my.pocketguard.R
import com.my.pocketguard.navigation.AppRoutes
import com.my.pocketguard.ui.theme.ButtonColor
import com.my.pocketguard.ui.theme.appTextStyle
import com.my.pocketguard.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun SplashView(navController: NavController) {
    val viewModel: AuthViewModel = hiltViewModel()
    val currentUser = viewModel.currentUser.collectAsState()
    val context = LocalContext.current
    val window = (context as Activity).window
    val offsetX = remember { Animatable(1000f) }
    val loadingOffsetX = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        hideStatusBar(window)
        if (currentUser.value != null) {
            delay(1500)
            val insetsController = WindowInsetsControllerCompat(window, window.decorView)
            insetsController.show(WindowInsetsCompat.Type.statusBars())
            insetsController.show(WindowInsetsCompat.Type.navigationBars())
            navController.navigate(AppRoutes.DASHBOARD.route) {
                popUpTo(0)
            }
        } else {
            loadingOffsetX.animateTo(
                targetValue = -1000f,
                animationSpec = tween(
                    durationMillis = 100,
                    delayMillis = 1500,
                    easing = LinearEasing
                )
            )
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    Scaffold(contentWindowInsets = WindowInsets(0.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash),
                contentDescription = "splash",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(300.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(100.dp))
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.offset {
                            IntOffset(
                                loadingOffsetX.value.roundToInt(),
                                0
                            )
                        },
                        color = ButtonColor
                    )
                    Button(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .offset { IntOffset(offsetX.value.roundToInt(), 0) },
                        colors = ButtonDefaults.buttonColors()
                            .copy(containerColor = ButtonColor),
                        onClick = {
                            viewModel.handleGoogleAuth(context, navController)
                        }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painterResource(id = R.drawable.google),
                                contentDescription = "",
                                modifier = Modifier.size(30.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                stringResource(R.string.google_login),
                                style = appTextStyle.copy(color = Color.White)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun hideStatusBar(window: Window){
    // 1. Let content extend into system windows
    WindowCompat.setDecorFitsSystemWindows(window, false)
    // 2. Get controller and hide bars
    val controller = WindowInsetsControllerCompat(window, window.decorView)
    controller.hide(WindowInsetsCompat.Type.systemBars()) // hides status + nav bars
    controller.hide(WindowInsetsCompat.Type.navigationBars()) // hides status + nav bars
    controller.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
}