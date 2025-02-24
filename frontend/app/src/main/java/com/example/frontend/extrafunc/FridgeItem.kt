package com.example.frontend

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun FridgeItem(ingredient: Ingredient, onStateChange: () -> Unit) {
    val dDay = ChronoUnit.DAYS.between(LocalDate.now(), ingredient.expiryDate).toInt()

    // 유통기한이 지나면 자동으로 WASTED 상태로 변경
    LaunchedEffect(dDay) {
        if (dDay < 0 && ingredient.state != IngredientState.WASTED) {
            ingredient.waste()
            onStateChange() // 상태 변경 후 UI 업데이트
        }
    }

    val dDayColor = when {
        dDay == 0 -> Color(0xFFD32F2F) // 당일 (빨강)
        dDay in 1..3 -> Color(0xFFFFA000) // 1~3일 (주황)
        else -> Color(0xFF388E3C) // 4일 이상 (초록)
    }

    Card(
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = getImageForCategory(ingredient.category)),
                    contentDescription = ingredient.name,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(bottom = 4.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = ingredient.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1045A1),
                    textAlign = TextAlign.Center
                )
            }

            // D-Day 표시
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp)
                    .background(dDayColor, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "D-$dDay",
                    fontSize = 10.sp,
                    color = Color.White
                )
            }

            // 소비 버튼 추가
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .background(Color(0xFF1976D2), RoundedCornerShape(4.dp))
                    .clickable {
                        ingredient.consume() // 상태를 소비로 변경
                        onStateChange() // UI 업데이트
                        Log.d("FridgeDebugItem", "소비됨: ${ingredient.name}")
                    }
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "소비",
                    fontSize = 10.sp,
                    color = Color.White
                )
            }


        }
    }
}
