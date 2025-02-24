package com.example.frontend

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.extrafunc.BottomNavigationBar
import com.example.frontend.extrafunc.CartIngredientRow
import com.example.frontend.extrafunc.DatePickerModal
import com.example.frontend.extrafunc.IngredientRow
import com.example.frontend.extrafunc.TopPopupMessage
import com.example.frontend.extrafunc.navigateSafely
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 냉장고 재료 리스트 (전역 변수)
var fridgeList = mutableStateListOf<Ingredient>()

// 장바구니 리스트 (전역 변수로 변경하여 상태 유지)
val cartList = mutableStateListOf<String>()

val ingredientList = mutableStateListOf<Ingredient>()

@Composable
fun IngredientScreen(navController: NavController
) {
    var selectedTabIndex by remember { mutableStateOf(-1) }
    var selfIngredient by remember { mutableStateOf(TextFieldValue("")) }
    var isTextFieldFocused by remember { mutableStateOf(false) }

    // 유통기한 및 식품군 관련 상태
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val foodCategories = listOf("가공식품", "간식", "신선식품", "어패류", "유제품", "육류", "음료", "조미식품","즉석식품")
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(foodCategories.first()) }

    // 상태 변수 (재료 목록 저장)
    //val ingredientList = remember { mutableStateListOf<Ingredient>() }

    // 장바구니 리스트 (재료 이름만 저장)
    //val cartList = remember { mutableStateListOf<String>() }


    // 팝업 상태 & 메시지
    var showPopup by remember { mutableStateOf(false) }
    var popupMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope() //CoroutineScope 선언



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

    // 팝업 메시지
    if (showPopup) {
        TopPopupMessage(message = "새로운 재료(${ingredientList.size})가 추가되었습니다") {
            showPopup = false
        }
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = R.drawable.alarm_icon),
            contentDescription = "알람",
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    //navController
                })
    }

    if (!isTextFieldFocused && selectedTabIndex == -1) {
        Box(
            modifier = Modifier
                .padding(top = 130.dp, start = 16.dp) // 고정 위치 설정
        ) {
            Text(
                text = "어떤 재료가 새로 들어왔나요?",
                fontSize = 25.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(900),
                color = Color(0xFF1A72D3),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }



    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(selectedTabIndex = if (selectedTabIndex >= 0) selectedTabIndex else 0,
            modifier = Modifier
                .padding(horizontal = 66.dp)
                .background(Color.Transparent),
            divider = {},
            contentColor = Color.Transparent,
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                if (selectedTabIndex >= 0) { // 선택된 탭이 있을 때만 인디케이터 적용
                    SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .background(Color.Transparent), color = Color(0xFFBBDEFA)
                    )
                }
            }) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0; isTextFieldFocused = true },
                modifier = Modifier
                    .height(47.dp)
                    .background(
                        color = if (selectedTabIndex == 0) Color(0xFFBBDEFA) else Color(0xFF90CAF8),
                        shape = RoundedCornerShape(
                            topStart = 10.dp, topEnd = 10.dp, bottomStart = 0.dp, bottomEnd = 0.dp
                        )
                    )
            ) {
                Text(
                    text = "직접 추가하기",
                    fontSize = 18.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF1045A1)
                )
            }
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                modifier = Modifier
                    .height(47.dp)
                    .background(
                        color = if (selectedTabIndex == 1) Color(0xFFBBDEFA) else Color(0xFF90CAF8),
                        shape = RoundedCornerShape(
                            topStart = 10.dp, topEnd = 10.dp, bottomStart = 0.dp, bottomEnd = 0.dp
                        )
                    )
            ) {
                Text(
                    text = "장바구니",
                    fontSize = 18.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF1045A1)
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(horizontal = 66.dp)
                .fillMaxWidth()
                .wrapContentHeight() // 고정 높이 제거하고 내용에 맞게 늘어나도록 설정
                .background(
                    color = Color(0xFFBBDEFA), shape = RoundedCornerShape(
                        topStart = 0.dp, topEnd = 0.dp, bottomStart = 10.dp, bottomEnd = 10.dp
                    )
                )
        ) {

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight() // 내부 `Box`도 동적으로 크기 변경
                    .background(
                        color = Color(0xFFEAF6FF), shape = RoundedCornerShape(size = 5.dp)
                    )
            ) {
                TextField(
                    value = selfIngredient,
                    onValueChange = { selfIngredient = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isTextFieldFocused = it.isFocused },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFEAF6FF),
                        focusedContainerColor = Color(0xFFEAF6FF),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color(0xFF1045A1),
                        unfocusedTextColor = Color(0xFF1045A1)
                    ),
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight(500)
                    ),
                    shape = RoundedCornerShape(size = 5.dp),
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.search_icon),
                            contentDescription = "search icon",
                            tint = Color(0xFF3FA4F4)
                        )
                    },
                    placeholder = {
                        Text(
                            text = "재료 입력...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight(500),
                            fontFamily = pretendard,
                            color = Color(0xFF1045A1)
                        )
                    },
                    singleLine = true
                )
            }

        }


        Spacer(modifier = Modifier.height(20.dp))


        if (selectedTabIndex == 0) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 66.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFBBDEFA), shape = RoundedCornerShape(size = 10.dp)
                        ), contentAlignment = Alignment.Center
                ) {
                    Column {
                        // 재료 입력
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp)
                                .background(Color(0xFFBBDEFA), RoundedCornerShape(10.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center // 가운데 정렬 적용
                        ) {
                            Text(
                                text = selfIngredient.text.ifEmpty { "재료 입력..." },
                                fontSize = 18.sp,
                                fontWeight = FontWeight(500),
                                fontFamily = pretendard,
                                color = Color(0xFF1045A1)
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))

                        // 유통기한
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .height(1.dp)
                                    .background(Color.White)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(55.dp)
                                    .background(Color.Transparent, RoundedCornerShape(10.dp)),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "유통기한",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight(500),
                                    fontFamily = pretendard,
                                    color = Color(0xFF1045A1),
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                                Button(
                                    onClick = { showDatePicker = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFF90CAF8
                                        )
                                    ), // 색상 변경
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(end = 5.dp) //버튼 옆으로 5dp 이동
                                ) {
                                    Text(
                                        text = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                        color = Color(0xFF1045A1) // 버튼 안의 글씨 색
                                    )
                                }
                                // DatePickerDialog 표시 (showDatePicker가 true일 때)
                                if (showDatePicker) {
                                    DatePickerModal(
                                        onDateSelected = { millis ->
                                            millis?.let {
                                                selectedDate = Instant.ofEpochMilli(it)
                                                    .atZone(ZoneId.systemDefault())
                                                    .toLocalDate()
                                            }
                                            showDatePicker = false
                                        },
                                        onDismiss = { showDatePicker = false }
                                    )
                                }

                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))

                        // 식품군
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .height(1.dp)
                                    .background(Color.White)
                                    .align(Alignment.CenterHorizontally)
                            )

                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(
                                    onClick = { expanded = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFFBBDEFA
                                        )
                                    )
                                ) {
                                    Text(
                                        selectedCategory,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight(500),
                                        fontFamily = pretendard,
                                        color = Color(0xFF1045A1)
                                    )
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                ) {
                                    foodCategories.forEach { category ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = category,
                                                    color = Color(0xFF1045A1),
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = pretendard
                                                )
                                            },
                                            onClick = {
                                                selectedCategory = category
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }


                        Spacer(modifier = Modifier.height(10.dp))


                        // 담기 버튼
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(
                                    color = Color(0xFF90CAF8), shape = RoundedCornerShape(
                                        bottomStart = 10.dp, bottomEnd = 10.dp
                                    )
                                )
                                .clickable {
                                    if (selfIngredient.text.isNotEmpty()) {
                                        val newIngredient = Ingredient(
                                            name = selfIngredient.text,
                                            addedDate = LocalDate.now(), // 현재 날짜를 추가일로 저장
                                            expiryDate = selectedDate,    // 선택한 유통기한 저장
                                            category = selectedCategory
                                        )
                                        ingredientList.add(newIngredient)

                                        // 콘솔에 로그 출력
                                        Log.d("IngredientLog", "추가된 재료: $newIngredient")

                                        // 입력 필드 초기화
                                        selfIngredient = TextFieldValue("")

                                        //선택 후 오늘 날짜로 다시 설정
                                        selectedDate = LocalDate.now()

                                    }
                                }, contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "담기",
                                fontSize = 18.sp,
                                fontWeight = FontWeight(600),
                                fontFamily = pretendard,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF1045A1)
                            )
                        }

                    }

                }

                Spacer(modifier = Modifier.height(10.dp))

                //재료 담기는 칸 (3개 이상부터 스크롤 가능)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFEAF6FF), shape = RoundedCornerShape(size = 10.dp)
                        )
                        .wrapContentHeight()
                ) {
                    Column {
                        if (ingredientList.isNotEmpty()) {
                            if (ingredientList.size > 3) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp) // 최대 높이 설정 (스크롤 가능)
                                ) {
                                    items(ingredientList) { ingredient ->
                                        IngredientRow(ingredient, ingredientList)
                                    }
                                }
                            } else {
                                ingredientList.forEach { ingredient ->
                                    IngredientRow(ingredient, ingredientList)
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(55.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "담긴 재료가 없습니다.",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight(500),
                                    fontFamily = pretendard,
                                    textAlign = TextAlign.Center,
                                    color = Color(0xFF1045A1)
                                )
                            }
                        }

                        // 추가하기 버튼
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(
                                    color = Color(0xFF90CAF8), shape = RoundedCornerShape(
                                        bottomStart = 10.dp, bottomEnd = 10.dp
                                    )
                                )
                                .clickable {
                                    if (ingredientList.isNotEmpty()) {
                                        fridgeList.addAll(ingredientList) // 담긴재료에서 냉장고 리스트로 이동

                                        // 팝업 메시지 업데이트
                                        popupMessage = "새로운 재료(${ingredientList.size})개가 추가되었습니다"
                                        showPopup = true

                                        //비동기적으로 실행하여 2초 후에 `clear()` 실행
                                        coroutineScope.launch {
                                            delay(500) // 2초 대기 후 실행
                                            ingredientList.clear() // 담긴 재료 비우기
                                        }
                                        //navController.navigate(Routes.FridgeScreen)


                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "추가하기",
                                fontSize = 18.sp,
                                fontWeight = FontWeight(600),
                                fontFamily = pretendard,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF1045A1)
                            )
                        }


                    }
                }


            }


        }

        // **장바구니 탭 UI**
        if (selectedTabIndex == 1) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 66.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFBBDEFA), shape = RoundedCornerShape(size = 10.dp)
                        ), contentAlignment = Alignment.Center
                ) {
                    Column {
                        // 재료 입력
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp)
                                .background(Color(0xFFBBDEFA), RoundedCornerShape(10.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = selfIngredient.text.ifEmpty { "재료 입력..." },
                                fontSize = 18.sp,
                                fontWeight = FontWeight(500),
                                fontFamily = pretendard,
                                color = Color(0xFF1045A1)
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))

                        // 담기 버튼
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(
                                    color = Color(0xFF90CAF8), shape = RoundedCornerShape(
                                        bottomStart = 10.dp, bottomEnd = 10.dp
                                    )
                                )
                                .clickable {
                                    if (selfIngredient.text.isNotEmpty()) {
                                        cartList.add(selfIngredient.text) // 장바구니에 추가
                                        selfIngredient = TextFieldValue("") // 입력 초기화
                                    }
                                }, contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "담기",
                                fontSize = 18.sp,
                                fontWeight = FontWeight(600),
                                fontFamily = pretendard,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF1045A1)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 장바구니 담기는 칸 (3개 이상부터 스크롤 가능)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFEAF6FF), shape = RoundedCornerShape(size = 10.dp)
                        )
                        .wrapContentHeight()
                ) {
                    Column {
                        if (cartList.isNotEmpty()) {
                            if (cartList.size > 3) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp) // 최대 높이 설정 (스크롤 가능)
                                ) {
                                    items(cartList) { ingredientName ->
                                        CartIngredientRow(ingredientName, cartList)
                                    }
                                }
                            } else {
                                cartList.forEach { ingredientName ->
                                    CartIngredientRow(ingredientName, cartList)
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(55.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "장바구니가 비어있습니다",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight(500),
                                    fontFamily = pretendard,
                                    textAlign = TextAlign.Center,
                                    color = Color(0xFF1045A1)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 구매완료 버튼
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(
                                    color = Color(0xFF90CAF8), shape = RoundedCornerShape(
                                        bottomStart = 10.dp, bottomEnd = 10.dp
                                    )
                                )
                                .clickable {
                                    if (cartList.isNotEmpty()) {
                                        cartList.clear() // 리스트 비우기 (추가 후 리셋)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "구매완료",
                                fontSize = 18.sp,
                                fontWeight = FontWeight(600),
                                fontFamily = pretendard,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF1045A1)
                            )
                        }
                    }
                }
            }
        }




        if (!isTextFieldFocused && selectedTabIndex == -1) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.paint(painter = painterResource(R.drawable.select_camera)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.CameraScreen)
                        },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier.size(200.dp),
                            painter = painterResource(R.drawable.camera_icon),
                            contentDescription = "camera icon",
                            tint = Color.White
                        )

                        Text(
                            text = "영수증 찍기",
                            fontSize = 25.sp,
                            fontFamily = pretendard,
                            fontWeight = FontWeight(700),
                            color = Color(0xFF1045A1)
                        )

                    }

                }



                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        navController.navigate(Routes.ReciptScreen)
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 60.dp)
                        .background(
                            color = Color(0xFF90CAF8), shape = RoundedCornerShape(size = 10.dp)
                        ), colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF90CAF8)
                    )
                ) {
                    Text(
                        text = "영수증 보기",
                        fontSize = 25.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight(700),
                        color = Color.White
                    )
                }

            }

        }


    }

    Column(verticalArrangement = Arrangement.Bottom) {
        Spacer(modifier = Modifier.weight(1f))
        BottomNavigationBar(
            selectedTab = "Ingredient", onTabSelected = {

            }, navController = navController
        )
    }

}


@Preview(showBackground = true)
@Composable
fun PreviewIngredientScreen() {
    IngredientScreen(navController = rememberNavController())
}