package com.example.frontend
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun DDayBar(
    //fridgeList: List<Ingredient>
    //historyList: SnapshotStateList<Ingredient>
) {
    val sortedIngredients = fridgeList
        .filter { it.state == IngredientState.FRESH } // 신선 상태만 필터링
        .sortedBy { it.expiryDate } // 유통기한이 빠른 순으로 정렬
        .take(3) // 가장 빠른 3개만 선택
    Log.d("FridgeListLog", "DDayBar에 전달된 fridgeList: $fridgeList")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Today",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A72D3)
        )

        sortedIngredients.forEach { ingredient ->
            val dDay = ChronoUnit.DAYS.between(LocalDate.now(), ingredient.expiryDate).toInt()

            //D-Day에 따른 색상 설정
            val dDayColor = when {
                dDay == 0 -> Color(0xFFD32F2F) // 당일 (빨강)
                dDay in 1..3 -> Color(0xFFFFA000) // 1~3일 (주황)
                else -> Color(0xFF388E3C) // 4일 이상 (초록)
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = getImageForCategory(ingredient.category)),
                    contentDescription = ingredient.name,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, shape = CircleShape)
                        .padding(5.dp)
                )
                Text(
                    text = "D-$dDay",
                    fontSize = 12.sp,
                    color = dDayColor //D-Day 색상 적용
                )
                Text(
                    text = ingredient.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A72D3)
                )
            }

        }
    }
}
