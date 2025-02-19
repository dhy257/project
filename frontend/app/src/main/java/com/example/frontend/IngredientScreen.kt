package com.example.frontend

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun IngredientScreen(navController: NavController) {
    var selectedTabIndex by remember { mutableStateOf(-1) }
    var selfIngredient by remember { mutableStateOf(TextFieldValue("")) }
    var isTextFieldFocused by remember { mutableStateOf(false) }

    // 유통기한 및 식품군 관련 상태
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val foodCategories = listOf("즉석식품", "음료", "가공식품", "조미식품", "유제품", "신선식품", "어패류", "육류")
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(foodCategories.first()) }


    // 재료 정보를 저장할 데이터 클래스
    data class Ingredient(
        val name: String,         // 제품명
        val category: String,     // 식품군
        val addedDate: LocalDate, // 제품 추가 날짜
        val expiryDate: LocalDate // 유통기한
    ) {
        // D-day 계산 (유통기한 - 추가한 날짜)
        fun getDday(): Long {
            return ChronoUnit.DAYS.between(addedDate, expiryDate)
        }
    }
    // 상태 변수 (재료 목록 저장)
    var ingredientList by remember { mutableStateOf(mutableListOf<Ingredient>()) }

    //장바구니 리스트 추가
    var cartList by remember { mutableStateOf(mutableListOf<Ingredient>()) }



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



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = R.drawable.alarm_icon),
            contentDescription = "뒤로 가기",
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    //navController
                })
    }

    Box() {
        Text(
            modifier = Modifier.padding(25.dp, 115.dp),
            text = "어떤 재료가 새로 들어왔나요?",
            fontSize = 25.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight(900),
            color = Color(0xFF1A72D3)
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

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
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF8)), // 색상 변경
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.wrapContentWidth()
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


                //재료 담기는 칸
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                        .background(
                            color = Color(0xFFEAF6FF), shape = RoundedCornerShape(size = 10.dp)
                        )
                ) {
                    Column {
                        ingredientList.forEach { ingredient ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .background(
                                            Color.Transparent, RoundedCornerShape(10.dp)
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = ingredient.name + "/D-" + ingredient.getDday() + "/" + ingredient.category,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight(500),
                                        fontFamily = pretendard,
                                        color = Color(0xFF1045A1),
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(8.dp)
                                    )

                                    IconButton(
                                        onClick = {
                                            ingredientList =
                                                ingredientList.filter { it != ingredient }
                                                    .toMutableList()
                                        }, modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = android.R.drawable.ic_delete),
                                            contentDescription = "삭제",
                                            tint = Color.Red
                                        )
                                    }
                                }

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
                                        cartList.addAll(ingredientList) //장바구니로 이동

                                        //리스트 비우기
                                        ingredientList = ingredientList.filter { false }.toMutableList()
                                    }
                                }, contentAlignment = Alignment.Center
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
                //장바구니 담기는 칸
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                        .background(
                            color = Color(0xFFEAF6FF), shape = RoundedCornerShape(size = 10.dp)
                        )
                ) {
                    Column {
                        cartList.forEach { ingredient ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .background(
                                            Color.Transparent, RoundedCornerShape(10.dp)
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = ingredient.name + "/D-" + ingredient.getDday() + "/" + ingredient.category,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight(500),
                                        fontFamily = pretendard,
                                        color = Color(0xFF1045A1),
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(8.dp)
                                    )

                                    IconButton(
                                        onClick = {
                                            cartList =
                                                cartList.filter { it != ingredient }
                                                    .toMutableList()
                                        }, modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = android.R.drawable.ic_delete),
                                            contentDescription = "삭제",
                                            tint = Color.Red
                                        )
                                    }
                                }

                            }
                        }
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
                                    cartList.clear() //장바구니 비우기
                                }, contentAlignment = Alignment.Center
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
                            //navController.navigate(Routes.CameraScreen)
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
                        //navController.navigate(Routes.ReciptScreen)
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
                if (it != "Profile") { // 오른쪽 버튼은 비활성화
                    navigateSafely(navController, it)
                }
            }, navController = navController
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewTestScreen() {
    IngredientScreen(navController = rememberNavController())
}