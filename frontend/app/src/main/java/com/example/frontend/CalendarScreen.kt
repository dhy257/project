package com.example.frontend

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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
import java.time.format.DateTimeFormatter


@Composable
fun CalendarScreen(navController: NavController) {
    val today = LocalDate.now()
    var selectedMonth by remember { mutableStateOf(today.monthValue) }
    val currentYear = today.year
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showPopup by remember { mutableStateOf(false) }

    val ingredientsByDate = remember { mutableStateMapOf<LocalDate, List<Ingredient>>() }
    ingredientsByDate.clear()
    fridgeList.groupBy { it.expiryDate }.forEach { (date, ingredients) ->
        ingredientsByDate[date] = ingredients
    }

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
                CalendarHeader(selectedMonth) { newMonth -> selectedMonth = newMonth }
                DaysOfWeekHeader()

                CalendarGrid(
                    selectedMonth = selectedMonth,
                    currentYear = currentYear,
                    today = today,
                    onDateSelected = { date ->
                        if (ingredientsByDate[date]?.isNotEmpty() == true) {
                            selectedDate = date
                            showPopup = true
                        }
                    },
                    ingredientsByDate = ingredientsByDate
                )
            }

        }
        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(
            selectedTab = "Main", onTabSelected = {
            }, navController = navController
        )
    }

    // 재료 목록 팝업
    if (showPopup && selectedDate != null) {
        AlertDialog(
            onDismissRequest = { showPopup = false },
            title = { Text(text = "${selectedDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} 재료 목록") },
            text = {
                Column {
                    ingredientsByDate[selectedDate]?.forEach { ingredient ->
                        Text(text = "• ${ingredient.name} (${ingredient.state})", fontSize = 16.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPopup = false }) {
                    Text("닫기")
                }
            }
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
fun CalendarGrid(
    selectedMonth: Int,
    currentYear: Int,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    ingredientsByDate: Map<LocalDate, List<Ingredient>>
) {
    val yearMonth = YearMonth.of(currentYear, selectedMonth)
    val daysInMonth = yearMonth.lengthOfMonth()
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
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(4.dp)
                    .clickable {
                        if (day.isNotEmpty()) {
                            val selectedDate = LocalDate.of(currentYear, selectedMonth, day.toInt())
                            onDateSelected(selectedDate)
                        }
                    },
                contentAlignment = Alignment.TopCenter // 모든 요소를 상단 정렬
            ) {
                if (day.isNotEmpty()) {
                    val date = LocalDate.of(currentYear, selectedMonth, day.toInt())
                    val isToday = date == today
                    val ingredients = ingredientsByDate[date] ?: emptyList()

                    Column(
                        modifier = Modifier.wrapContentHeight(), // 최소 높이 유지
                        verticalArrangement = Arrangement.Top, // 모든 요소를 상단에 정렬
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // "Today" 텍스트를 맨 위에 정렬
                        if (isToday) {
                            Text(
                                text = "Today",
                                fontSize = 10.sp,
                                fontFamily = pretendard,
                                fontWeight = FontWeight(500),
                                color = Color(0xFF1A72D3),
                                modifier = Modifier.padding(bottom = 0.dp) // 날짜와 간격 조정
                            )
                        }

                        // 날짜 표시
                        Text(
                            text = day,
                            fontSize = 16.sp,
                            fontFamily = pretendard,
                            fontWeight = FontWeight(400),
                            color = if (isToday) Color(0xFF1A72D3) else Color.Black
                        )

                        Spacer(modifier = Modifier.height(2.dp)) // 날짜와 줄 사이 여백 추가

                        // **재료 상태 밑줄 (최대 3개까지 표시)**
                        if (ingredients.isNotEmpty()) {
                            val displayedIngredients = ingredients.take(3) // 최대 3개까지만 표시

                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f) // 줄 길이 조정
                                    .height(10.dp) // 줄 높이 유지
                            ) {
                                val lineHeight = 10f // 선 굵기 조정
                                val lineSpacing = lineHeight + 3f // 줄 간격 조정

                                displayedIngredients.forEachIndexed { index, ingredient ->
                                    val lineY = index * lineSpacing // 간격 조절
                                    drawLine(
                                        color = categoryColorMap[ingredient.category] ?: Color.Gray,
                                        start = Offset(0f, lineY),
                                        end = Offset(size.width, lineY),
                                        strokeWidth = lineHeight
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// **카테고리별 색상 지정**
val categoryColorMap = mapOf(
    "가공식품" to Color(0xFFFF5733),  // 오렌지
    "간식" to Color(0xFFFFD700),  // 노랑
    "신선식품" to Color(0xFF4CAF50),  // 초록
    "어패류" to Color(0xFF1E90FF),  // 파랑
    "유제품" to Color(0xFF8A2BE2),  // 보라
    "육류" to Color(0xFFA52A2A),  // 갈색
    "음료" to Color(0xFF00CED1),  // 청록
    "조미식품" to Color(0xFFFF69B4),  // 핑크
    "즉석식품" to Color(0xFF808080)   // 회색
)
@Preview(showBackground = true)
@Composable
fun PreviewCalendarScreen() {
    // 테스트용 재료 데이터
    fridgeList.clear()
    fridgeList.addAll(
        listOf(
            Ingredient(
                name = "재료1",
                addedDate = LocalDate.now().minusDays(5), // 5일 전에 추가
                expiryDate = LocalDate.now().minusDays(1), // 1일 전에 유통기한 만료
                category = "가공식품",
                state = IngredientState.CONSUMED
            ),
            Ingredient(
                name = "재료1",
                addedDate = LocalDate.now().minusDays(7), // 7일 전에 추가
                expiryDate = LocalDate.now().minusDays(2),
                category = "간식",
                state = IngredientState.WASTED
            ),
            Ingredient(
                name = "재료3",
                addedDate = LocalDate.now().minusDays(10), // 10일 전에 추가
                expiryDate = LocalDate.now().minusDays(3),
                category = "신선식품",
                state = IngredientState.WASTED
            ),
            Ingredient(
                name = "재료4",
                addedDate = LocalDate.now().minusDays(0), // 10일 전에 추가
                expiryDate = LocalDate.now().minusDays(0),
                category = "신선식품",
                state = IngredientState.WASTED
            ),
            Ingredient(
                name = "재료4",
                addedDate = LocalDate.now().minusDays(0), // 10일 전에 추가
                expiryDate = LocalDate.now().minusDays(0),
                category = "유제품",
                state = IngredientState.WASTED
            ),
            Ingredient(
                name = "재료4",
                addedDate = LocalDate.now().minusDays(0), // 10일 전에 추가
                expiryDate = LocalDate.now().minusDays(0),
                category = "육류",
                state = IngredientState.WASTED
            ),



            )
    )

    CalendarScreen(navController = rememberNavController())
}