package com.example.frontend.extrafunc

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.frontend.pretendard

// 장바구니에 추가된 재료 UI
@Composable
fun CartIngredientRow(ingredientName: String, list: MutableList<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = ingredientName,
            fontSize = 18.sp,
            fontWeight = FontWeight(500),
            fontFamily = pretendard,
            color = Color(0xFF1045A1),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )

        IconButton(
            onClick = { list.remove(ingredientName) },
            modifier = Modifier.size(30.dp)
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_delete),
                contentDescription = "삭제",
                tint = Color.Red
            )
        }
    }
}