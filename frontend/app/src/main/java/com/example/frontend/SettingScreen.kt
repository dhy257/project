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
fun SettingScreen(navController: NavController){
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
                text = "원하는 알림 방식을 선택해주세요",
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

            }

        }

        //하단 네비게이션 바 추가
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 0.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            BottomNavigationBar(
                selectedTab = "Profile",
                onTabSelected = {

                },
                navController = navController
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingScreen() {
    SettingScreen(navController = rememberNavController())
}
