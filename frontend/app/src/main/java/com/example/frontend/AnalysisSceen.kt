package com.example.frontend

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.frontend.extrafunc.navigateSafely

@Composable
fun AnalysisScreen(navController: NavController) {
    val categoryScores = remember { calculateCategoryScores(fridgeList) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        //배경 이미지
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                ),
            painter = painterResource(R.drawable.main_screen), // 배경 설정
            contentDescription = null,
            contentScale = ContentScale.Crop
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "카테고리 별 소비 점수",
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
                    .padding(bottom = 100.dp)
            ) {
                LazyColumn {
                    items(categoryScores) { categoryScore ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    selectedCategory = categoryScore.category // 다이얼로그 열기
                                },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = categoryScore.category,
                                    fontSize = 20.sp,
                                    fontFamily = pretendard,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "총 재료 수: ${categoryScore.totalItems}",fontFamily = pretendard)
                                Text(text = "소비: ${categoryScore.consumedItems}, 낭비: ${categoryScore.wastedItems}, 신선: ${categoryScore.freshItems}",fontFamily = pretendard)
                                Text(
                                    text = "점수: ${categoryScore.score}점",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = pretendard,
                                    color = Color(0xFF1E88E5)
                                )
                            }
                        }
                    }
                }
            }

        }

        //카테고리 다이얼로그 표시
        selectedCategory?.let { category ->
            AnalysisDetailDialog(category = category, ingredientList = fridgeList, onDismiss = { selectedCategory = null })
        }

        //하단 네비게이션 바 추가
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 0.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            BottomNavigationBar(
                selectedTab = "Main",
                onTabSelected = {
                },
                navController = navController
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAnalysisScreen() {
    AnalysisScreen(navController = rememberNavController())
}

