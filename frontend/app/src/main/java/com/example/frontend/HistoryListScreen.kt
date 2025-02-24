package com.example.frontend

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.time.LocalDate

@Preview(showBackground = true)
@Composable
fun PreviewConsumedListScreen() {
    val sampleConsumedList = remember {
        mutableStateListOf(
            Ingredient(
                name = "재료1",
                addedDate = LocalDate.now().minusDays(5), // 5일 전에 추가
                expiryDate = LocalDate.now().minusDays(1), // 1일 전에 유통기한 만료
                category = "카테고리1",
                state = IngredientState.CONSUMED
            ),
            Ingredient(
                name = "재료1",
                addedDate = LocalDate.now().minusDays(7), // 7일 전에 추가
                expiryDate = LocalDate.now().minusDays(2),
                category = "카테고리1",
                state = IngredientState.WASTED
            ),
            Ingredient(
                name = "재료3",
                addedDate = LocalDate.now().minusDays(10), // 10일 전에 추가
                expiryDate = LocalDate.now().minusDays(3),
                category = "카테고리3",
                state = IngredientState.WASTED
            ),
            Ingredient(
                name = "재료4",
                addedDate = LocalDate.now().minusDays(10), // 10일 전에 추가
                expiryDate = LocalDate.now().minusDays(3),
                category = "카테고리3",
                state = IngredientState.WASTED
            )
        )
    }
    HistoryListScreen(navController = rememberNavController()
        , historyList = sampleConsumedList)
}

@Composable
fun HistoryListScreen(navController: NavController, historyList: SnapshotStateList<Ingredient>) {

    // 소비/낭비 상태인 재료만 필터링하여 카테고리별 그룹화
    val groupedHistory = historyList
        .filter { it.state == IngredientState.CONSUMED || it.state == IngredientState.WASTED }
        .groupBy { it.category }


    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "소비/낭비 내역",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A72D3),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // 뒤로 가기 버튼 추가
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF8))
        ) {
            Text(text = "뒤로 가기", color = Color.White, fontSize = 18.sp)
        }

        if (groupedHistory.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("소비/낭비된 재료가 없습니다.", fontSize = 18.sp, fontWeight = FontWeight(500), color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                groupedHistory.forEach { (category, items) ->
                    item {
                        Column {
                            Text(
                                text = "$category (${items.size})",
                                fontSize = 20.sp,
                                fontWeight = FontWeight(500),
                                color = Color(0xFF1A72D3),
                                modifier = Modifier.padding(8.dp)
                            )

                            // LazyVerticalGrid를 사용하여 카드 형식으로 표시
                            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(3),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 350.dp) // 스크롤 가능
                                ) {
                                    // **카테고리별 아이템만 전달하여 중복 방지**
                                    items(items.sortedBy { it.expiryDate }, key = { it.id }) { ingredient ->
                                        HistoryItem(ingredient)
                                    }
                                }
                            }
                        }
                    }
                }

            }

        }
    }
}