package com.example.frontend

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.example.frontend.extrafunc.BottomNavigationBar
import com.example.frontend.extrafunc.navigateSafely
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarScreen(navController: NavController) {
    var selectedMonth by remember { mutableStateOf(LocalDate.now().monthValue) }
    val today = LocalDate.now()
    val currentYear = today.year

    BackHandler {
        // 뒤로 가기 방지
    }

    Image(
        modifier = Modifier
            .fillMaxSize()
            //하단 바 맞추기
            .padding(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            ),
        painter = painterResource(R.drawable.calendar_background),
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
            painter = painterResource(id = R.drawable.back_arrow),
            contentDescription = "뒤로 가기",
            modifier = Modifier
                .size(40.dp)
                .clickable { navController.popBackStack() }
        )
    }

    Box {
        Text(
            modifier = Modifier.padding(25.dp, 115.dp),
            text = "소비기한이 다가오고 있어요",
            fontSize = 25.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight(900),
            color = Color(0xFF1A72D3)
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(185.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = Color.White, shape = RoundedCornerShape(size = 20.dp))
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 상단 바 (월 선택)
                CalendarHeader(selectedMonth) { newMonth ->
                    selectedMonth = newMonth
                }

                // 요일 표시
                DaysOfWeekHeader()

                // 캘린더 날짜 표시
                CalendarGrid(selectedMonth, currentYear, today)
            }

        }
        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(
            selectedTab = "Main", onTabSelected = {
                if (it != "Profile") { // 오른쪽 버튼은 비활성화
                    navigateSafely(navController, it)
                }
            }, navController = navController
        )
    }

}

/** 캘린더 상단 (월 선택 + 오늘 날짜로 이동 버튼) */
@Composable
fun CalendarHeader(selectedMonth: Int, onMonthSelected: (Int) -> Unit) {
    val today = LocalDate.now().monthValue
    val previousMonth = if (selectedMonth == 1) 12 else selectedMonth - 1
    val nextMonth = if (selectedMonth == 12) 1 else selectedMonth + 1

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // "오늘 보기" 버튼 추가
        if (selectedMonth != today) { // 현재 월이 아니면 버튼 표시
            Box(
                modifier = Modifier
                    .background(Color(0xFF63B4F6), shape = RoundedCornerShape(10.dp))
                    .clickable { onMonthSelected(today) }
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "오늘 보기",
                    fontSize = 14.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            // 이전 월 (불투명 처리)
            Text(
                text = "$previousMonth 월",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray.copy(alpha = 0.5f), // 반투명 처리
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { onMonthSelected(previousMonth) }
            )

            // 현재 월 (가운데 정렬)
            Text(
                text = "$selectedMonth 월",
                fontSize = 20.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(600),
                color = Color(0xFF1A72D3),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // 다음 월 (불투명 처리)
            Text(
                text = "$nextMonth 월",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray.copy(alpha = 0.5f), // 반투명 처리
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { onMonthSelected(nextMonth) }
            )
        }

    }
}

/** 요일 표시 (일 ~ 토) */
@Composable
fun DaysOfWeekHeader() {
    val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")

    // 위쪽 선 추가
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f) // 선 길이 조정
            .height(1.dp)
            .background(Color(0xFF90CAF8))
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        daysOfWeek.forEach { day ->
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    fontSize = 16.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF1A72D3)
                )
            }
        }

    }

    // 아래쪽 선 추가
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f) // 선 길이 조정
            .height(1.dp)
            .background(Color(0xFF90CAF8))
    )
}

/** 캘린더 날짜 그리드 */
@Composable
fun CalendarGrid(selectedMonth: Int, currentYear: Int, today: LocalDate) {
    val yearMonth = YearMonth.of(currentYear, selectedMonth)
    val daysInMonth = yearMonth.lengthOfMonth() // 월의 일 수 자동 계산
    val firstDayOfMonth = yearMonth.atDay(1).dayOfWeek.value % 7 // 0 = 일요일
    val totalDays = (1..daysInMonth).map { it.toString() }.toMutableList()

    repeat(firstDayOfMonth) { totalDays.add(0, "") } // 첫 요일 맞추기

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(totalDays) { day ->
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                if (day.isNotEmpty()) {
                    val isToday =
                        today.dayOfMonth.toString() == day && today.monthValue == selectedMonth

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // Today 라벨
                        if (isToday) {
                            Text(
                                text = "Today",
                                fontSize = 10.sp,
                                fontWeight = FontWeight(500),
                                color = Color(0xFF1A72D3)
                            )
                        }

                        // 날짜 텍스트
                        Text(
                            text = day,
                            fontSize = 16.sp,
                            fontFamily = pretendard,
                            fontWeight = FontWeight(400),
                            color = if (isToday) Color(0xFF1A72D3) else Color.Black
                        )


                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalendar() {
    CalendarScreen(navController = rememberNavController())
}