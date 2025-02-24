package com.example.frontend

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AnalysisDetailDialog(
    category: String,
    ingredientList: List<Ingredient>,
    onDismiss: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    // **정렬 로직**
    val categoryItems = ingredientList.filter { it.category == category }
        .sortedWith(
            compareBy<Ingredient> { it.state.ordinal } // 1️⃣ 상태 기준 (FRESH → CONSUMED → WASTED)
                .thenBy { it.stateChangeDate ?: LocalDate.MAX } // 2️⃣ 같은 상태일 때 날짜가 빠른 순 정렬
        )

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$category 상세 분석",
                    fontSize = 22.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    items(categoryItems) { ingredient ->
                        val statusText = when (ingredient.state) {
                            IngredientState.CONSUMED -> "소비됨"
                            IngredientState.WASTED -> "낭비됨"
                            else -> "신선함"
                        }
                        val changeDateText = ingredient.stateChangeDate?.format(dateFormatter) ?: "변경 없음"

                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = when (ingredient.state) {
                                    IngredientState.CONSUMED -> Color(0xFFC8E6C9) // 초록 (소비)
                                    IngredientState.WASTED -> Color(0xFFFFCDD2) // 빨강 (낭비)
                                    else -> Color(0xFFFFF9C4) // 노랑 (신선)
                                }
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${ingredient.name} - $statusText ($changeDateText)",
                                    fontSize = 16.sp,
                                    fontFamily = pretendard,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 닫기 버튼
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF8))
                ) {
                    Text(text = "닫기", color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryDetailDialog() {
    val sampleIngredients = listOf(
        Ingredient(
            name = "소고기",
            category = "육류",
            addedDate = LocalDate.now().minusDays(5),
            expiryDate = LocalDate.now().plusDays(5),
            state = IngredientState.FRESH
        ),
        Ingredient(
            name = "돼지고기",
            category = "육류",
            addedDate = LocalDate.now().minusDays(10),
            expiryDate = LocalDate.now().minusDays(2),
            state = IngredientState.CONSUMED,
            stateChangeDate = LocalDate.of(2024, 2, 12) // 소비 날짜
        ),
        Ingredient(
            name = "닭고기",
            category = "육류",
            addedDate = LocalDate.now().minusDays(20),
            expiryDate = LocalDate.now().minusDays(5),
            state = IngredientState.WASTED,
            stateChangeDate = LocalDate.of(2023, 9, 23) // 낭비 날짜
        ),
        Ingredient(
            name = "양고기",
            category = "육류",
            addedDate = LocalDate.now().minusDays(15),
            expiryDate = LocalDate.now().minusDays(5),
            state = IngredientState.FRESH
        ),
        Ingredient(
            name = "오리고기",
            category = "육류",
            addedDate = LocalDate.now().minusDays(12),
            expiryDate = LocalDate.now().minusDays(6),
            state = IngredientState.CONSUMED,
            stateChangeDate = LocalDate.of(2024, 1, 20) // 소비 날짜
        )
    )

    AnalysisDetailDialog(category = "육류", ingredientList = sampleIngredients, onDismiss = {})
}