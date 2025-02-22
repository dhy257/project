package com.example.frontend.extrafunc

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.example.frontend.ingredientList

@Composable
fun TopPopupMessage(message: String, onDismiss: () -> Unit) {
    var isVisible by remember { mutableStateOf(true) }

    if (isVisible) {
        LaunchedEffect(Unit) {
            delay(500) // 2초 후 자동 닫힘
            isVisible = false
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { -100 }) + fadeIn(),
        exit = fadeOut(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp) // 상단에 고정
            .zIndex(10f) // 항상 최상단 유지
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .background(Color(0xFF1D85E6), RoundedCornerShape(30.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = message, color = Color.White, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTopPopupMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        TopPopupMessage(message = "새로운 재료(${ingredientList.size})가 추가되었습니다") {}
    }
}