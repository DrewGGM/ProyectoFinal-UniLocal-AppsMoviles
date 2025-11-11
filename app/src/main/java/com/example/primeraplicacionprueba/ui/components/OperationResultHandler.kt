package com.example.primeraplicacionprueba.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.ui.theme.ErrorRed
import com.example.primeraplicacionprueba.ui.theme.SuccessGreen
import com.example.primeraplicacionprueba.utils.RequestResult
import kotlinx.coroutines.delay

@Composable
fun OperationResultHandler(
    result: RequestResult?,
    onSuccess: suspend () -> Unit,
    onFailure: suspend () -> Unit
) {
    var showToast by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = result) {
        when (result) {
            is RequestResult.Success -> {
                showToast = true
                delay(timeMillis = 2000)
                showToast = false
                delay(timeMillis = 200)
                onSuccess()
            }
            is RequestResult.Failure -> {
                showToast = true
                delay(timeMillis = 4000)
                showToast = false
                delay(timeMillis = 200)
                onFailure()
            }
            is RequestResult.Loading -> {
                showToast = false
            }
            else -> {
                showToast = false
            }
        }
    }

    // Show loading indicator
    when (result) {
        is RequestResult.Loading -> {
            LinearProgressIndicator()
        }
        else -> {}
    }

    // Toast overlay
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = showToast && result != null && result !is RequestResult.Loading,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(250)
            ) + fadeIn(animationSpec = tween(250)),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(250)
            ) + fadeOut(animationSpec = tween(250))
        ) {
            when (result) {
                is RequestResult.Success -> {
                    Toast(message = result.message, isSuccess = true)
                }
                is RequestResult.Failure -> {
                    Toast(message = result.errorMessage, isSuccess = false)
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun Toast(
    message: String,
    isSuccess: Boolean
) {
    Box(
        modifier = Modifier
            .padding(bottom = 80.dp, start = 24.dp, end = 24.dp)
            .widthIn(min = 200.dp, max = 400.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = if (isSuccess) SuccessGreen else ErrorRed,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = message,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}