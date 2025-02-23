package com.example.frontend

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.example.frontend.extrafunc.BottomNavigationBar
import com.example.frontend.extrafunc.navigateSafely
import androidx.lifecycle.viewmodel.compose.viewModel

val historyListState = mutableStateListOf<Ingredient>()  // 소비,낭비된 재료 리스트 추가

@Composable
fun FridgeScreen(
    navController: NavController
) {
    // 상태를 SnapshotStateList로 관리
    val fridgeListState = remember { mutableStateListOf<Ingredient>().apply { addAll(fridgeList) } }

    val groupedIngredients = fridgeListState
        .filter { it.state == IngredientState.FRESH } // 신선 상태만 필터링
        .groupBy { it.category }


    // 유통기한이 지난 재료 자동 낭비 처리 (중복 방지 적용)
    LaunchedEffect(fridgeListState) {
        fridgeListState.forEach { ingredient ->
            if (ingredient.getDday() < 0 && ingredient.state != IngredientState.WASTED) {
                ingredient.waste()

                // 기존 ID가 존재하는 경우 제거 후 추가
                historyListState.removeIf { it.id == ingredient.id }
                historyListState.add(ingredient)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                ),
            painter = painterResource(R.drawable.main_screen),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "냉장고 재료 관리",
                fontSize = 25.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF1A72D3),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            Button(
                onClick = {
                    // 소비/낭비 내역 보기 화면으로 이동
                    //navController.navigate(Routes.HistoryListScreen)
                          },
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A72D3))
            ) {
                Text(text = "소비/낭비 내역 보기", color = Color.White, fontSize = 18.sp)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 70.dp)
            ) {
                if (groupedIngredients.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "냉장고가 비어 있습니다.",
                            fontSize = 18.sp,
                            fontWeight = FontWeight(500),
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        groupedIngredients.forEach { (category, items) ->
                            item {
                                Column {

                                    Text(
                                        text = "$category (${items.size})",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight(500),
                                        color = Color(0xFF1A72D3),
                                        modifier = Modifier.padding(8.dp)
                                    )

                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(3),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp)
                                            .heightIn(max = 350.dp)
                                    ) {
                                        items(items.sortedBy { it.expiryDate }) { ingredient ->
                                            FridgeItem(ingredient) {
                                                // UI 업데이트를 위해 상태 변경 후 리스트 갱신
                                                ingredient.consume()
                                                fridgeListState.remove(ingredient)
                                                fridgeListState.add(ingredient) // 변경된 상태 반영
                                                if (ingredient.state == IngredientState.CONSUMED || ingredient.state == IngredientState.WASTED) {
                                                    historyListState.removeIf { it.id == ingredient.id }
                                                    historyListState.add(ingredient)
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
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 0.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            BottomNavigationBar(
                selectedTab = "Main",
                onTabSelected = {
                    if (it != "Profile") {
                        navigateSafely(navController, it)
                    }
                },
                navController = navController
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFridgeScreen() {
    FridgeScreen(navController = rememberNavController())
}