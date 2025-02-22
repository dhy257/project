package com.example.frontend

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.frontend.extrafunc.BottomNavigationBar
import com.example.frontend.extrafunc.getImageForCategory
import com.example.frontend.extrafunc.navigateSafely
import java.time.LocalDate
import java.time.temporal.ChronoUnit


@Preview(showBackground = true)
@Composable
fun PreviewFridgeScreen() {
    FridgeScreen(navController = rememberNavController())
}

@Composable
fun FridgeScreen(navController: NavController) {
    val groupedIngredients by remember { mutableStateOf(fridgeList.groupBy { it.category }) }
    val expandedState = remember { mutableStateMapOf<String, Boolean>() }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
            painter = painterResource(R.drawable.main_screen),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            //냉장고 재료 관리 텍스트 항상 표시
            Text(
                text = "냉장고 재료 관리",
                fontSize = 25.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(600),
                color = Color(0xFF1A72D3),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 70.dp)
            ) {
                if (groupedIngredients.isEmpty()) {
                    //냉장고가 비었을 때 메시지 표시
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "냉장고가 비어 있습니다.",
                            fontSize = 18.sp,
                            fontFamily = pretendard,
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
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Transparent, RoundedCornerShape(10.dp))
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "$category (${items.size})",
                                                fontSize = 20.sp,
                                                fontFamily = pretendard,
                                                fontWeight = FontWeight(500),
                                                color = Color(0xFF1A72D3),
                                                modifier = Modifier.weight(1f)
                                            )
                                            Button(
                                                onClick = {
                                                    expandedState[category] =
                                                        !(expandedState[category] ?: false)
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color(0xFF90CAF8)
                                                )
                                            ) {
                                                Text(
                                                    text = if (expandedState[category] == true) "접기" else "자세히",
                                                    color = Color.White
                                                )
                                            }
                                        }

                                        //유통기한이 짧은 순으로 정렬 후 표시
                                        if (expandedState[category] == true) {
                                            LazyVerticalGrid(
                                                columns = GridCells.Fixed(3),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = 8.dp)
                                                    .heightIn(max = 350.dp)
                                            ) {
                                                items(items.sortedBy { it.expiryDate }) { ingredient ->
                                                    FridgeItem(ingredient)
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
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
            selectedTab = "Ingredient",
            onTabSelected = {
                if (it != "Profile") {
                    navigateSafely(navController, it)
                }
            },
            navController = navController
        )
    }
}

@Composable
fun FridgeItem(ingredient: Ingredient) {
    val dDay = ChronoUnit.DAYS.between(LocalDate.now(), ingredient.expiryDate).toInt()

    //D-Day에 따른 색상 설정
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
                    color = Color(0xFF1045A1),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}