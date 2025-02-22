
package com.example.frontend

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.frontend.extrafunc.BottomNavigationBar
import com.example.frontend.extrafunc.DDayBar

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen(
        navController = rememberNavController()
    )
}

@Composable
fun MainScreen(
    navController: NavController
) {


    var selectedTab by remember { mutableStateOf("Main") }

    var fridgeList by remember { mutableStateOf(fridgeList) }

    BackHandler {
        // 뒤로 가기 버튼을 눌렀을 때 아무 동작도 하지 않도록 설정
    }

    Image(
        modifier = Modifier.fillMaxSize()
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
        painter = painterResource(R.drawable.main_screen),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.alarm_icon),
            contentDescription = "알람 버튼",
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    //navController
                }
        )
    }

    Box {
        Text(
            modifier = Modifier.padding(25.dp, 115.dp),
            text = "식재료 관리하기",
            fontSize = 30.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight(900),
            color = Color(0xFF1A72D3)
        )
    }

    LaunchedEffect(fridgeList) {
        Log.d("FridgeListLog", "현재 냉장고 리스트: $fridgeList")
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(190.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = Color(0xFFEAF6FF))
        ) {
            DDayBar(fridgeList) // 냉장고 리스트에서 유통기한 가까운 3개 표시
        }

    }



    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(372.dp))
        Row {
            Box(modifier = Modifier
                .size(115.dp)
                .background(
                    color = Color(0xFFEAF6FF), shape = RoundedCornerShape(size = 20.dp)
                )
                .clickable {

                }) {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = 29.71.dp, vertical = 16.05.dp)
                        .width(60.79427.dp)
                        .height(72.08463.dp),
                    painter = painterResource(R.drawable.analysis_icon),
                    contentDescription = "Analysis Icon",
                    tint = Color(0xFF3FA4F4)
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(10.dp),
                    text = "Analysis",
                    fontSize = 15.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF3FA4F4)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(modifier = Modifier
                .size(115.dp)
                .background(
                    color = Color(0xFFEAF6FF), shape = RoundedCornerShape(size = 20.dp)
                )
                .clickable {
                    //navController.navigate(Routes.CalendarScreen)
                    navController.navigate(Routes.CalendarScreen)
                }) {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = 29.71.dp, vertical = 16.05.dp)
                        .width(60.79427.dp)
                        .height(72.08463.dp),
                    painter = painterResource(R.drawable.calendar_icon),
                    contentDescription = "Analysis Icon",
                    tint = Color(0xFF3FA4F4)
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(10.dp),
                    text = "Calendar",
                    fontSize = 15.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF3FA4F4)
                )
            }


        }

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Box(modifier = Modifier
                .size(115.dp)
                .background(
                    color = Color(0xFFEAF6FF), shape = RoundedCornerShape(size = 20.dp)
                )
                .clickable {

                }) {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = 29.71.dp, vertical = 16.05.dp)
                        .width(60.79427.dp)
                        .height(72.08463.dp),
                    painter = painterResource(R.drawable.sharing_icon),
                    contentDescription = "Analysis Icon",
                    tint = Color(0xFF3FA4F4)
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(10.dp),
                    text = "Sharing",
                    fontSize = 15.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF3FA4F4)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(modifier = Modifier
                .size(115.dp)
                .background(
                    color = Color(0xFFEAF6FF), shape = RoundedCornerShape(size = 20.dp)
                )
                .clickable {
                    navController.navigate(Routes.FridgeScreen)
                }) {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = 29.71.dp, vertical = 16.05.dp)
                        .width(60.79427.dp)
                        .height(72.08463.dp),
                    painter = painterResource(R.drawable.ingredient_icon),
                    contentDescription = "Analysis Icon",
                    tint = Color(0xFF3FA4F4)
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(10.dp),
                    text = "Ingredient",
                    fontSize = 15.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF3FA4F4)
                )
            }

        }

        Spacer(modifier = Modifier.weight(1f))

        // 하단 네비게이션 바 추가
        BottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            navController = navController
        )

    } //column

}
