package com.example.frontend.extrafunc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.Ingredient
import com.example.frontend.pretendard

// 재료 항목 UI 및 삭제 기능
@Composable
fun IngredientRow(ingredient: Ingredient, list: MutableList<Ingredient>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    Color.Transparent, RoundedCornerShape(10.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${ingredient.name} / D-${ingredient.getDday()} / ${ingredient.category}",
                fontSize = 18.sp,
                fontWeight = FontWeight(500),
                fontFamily = pretendard,
                color = Color(0xFF1045A1),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )

            IconButton(
                onClick = {
                    // 동일한 요소가 여러 개 있을 경우 하나만 삭제하도록 변경
                    val index = list.indexOfFirst { it.name == ingredient.name && it.expiryDate == ingredient.expiryDate && it.category == ingredient.category }
                    if (index != -1) {
                        list.removeAt(index)
                    }
                }, modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_delete),
                    contentDescription = "삭제",
                    tint = Color.Red
                )
            }
        }
    }
}