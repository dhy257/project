package com.example.frontend

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Preview(showBackground = true)
@Composable
fun PreviewReciptScreen() {
    ReciptScreen(navController = rememberNavController()) // 네비게이션 컨트롤러 추가
}

@Composable
fun ReciptScreen(
    navController: NavController
) {

    Image(
        modifier = Modifier
            .fillMaxSize()
            //하단 바 맞추기
            .padding(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            ),
        painter = painterResource(R.drawable.main_screen),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    BackHandler(
    ) { }

    IconButton(modifier = Modifier.padding(
        15.dp
    ),
        onClick = {
            navController.navigate(Routes.MainScreen)
        }
    ) {
        Icon(
            modifier = Modifier.size(35.dp),
            painter = painterResource(R.drawable.back_arrow),
            contentDescription = null,
            tint = Color.White,
        )
    }

    Box {
        Text(
            modifier = Modifier.padding(25.dp, 115.dp),
            text = "영수증화면",
            fontSize = 25.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight(900),
            color = Color(0xFF1A72D3)
        )
    }

    val receiptText = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("receiptText") ?: "OCR 결과 없음"

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .wrapContentSize()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("영수증 내역", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = receiptText, // OCR 결과를 그대로 표시
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(onClick = { navController.popBackStack() }) {
                    Text("뒤로가기")
                }
            }
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(
            selectedTab = "CalendarScreen",
            onTabSelected = {
                if (it != "Profile") { // 오른쪽 버튼은 비활성화
                    navigateSafely(navController, it)
                }
            },
            navController = navController
        )
    }


}


