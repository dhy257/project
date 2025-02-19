
package com.example.frontend

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    navController: NavController
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            //하단 바 맞추기
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 좌측 버튼: IngredientScreen으로 이동
        NavigationButton(
            iconRes = R.drawable.ingredient_bottom_icon,
            isSelected = selectedTab == "Ingredient",
            onClick = {
                onTabSelected("Ingredient")
                //navigateSafely(navController, Routes.test)
            }
        )

        Spacer(modifier = Modifier.width(15.dp))

        // 중앙 버튼: MainScreen으로 이동
        NavigationButton(
            iconRes = R.drawable.cart_bottom_icon,
            isSelected = selectedTab == "Main",
            onClick = {
                onTabSelected("Main")
                navigateSafely(navController, Routes.MainScreen)
            }
        )

        Spacer(modifier = Modifier.width(15.dp))

        // 우측 버튼: 클릭 불가능 (비활성화)
        NavigationButton(
            iconRes = R.drawable.profile_bottom_icon,
            isSelected = selectedTab == "Profile",
            onClick = {
                //
            }
        )
    }
}

/** 안전한 네비게이션 함수 */
fun navigateSafely(navController: NavController, route: String) {
    try {
        navController.navigate(route)
    } catch (e: Exception) {
        Log.e("NavigationError", "Navigation to $route failed: ${e.message}")
    }
}

/** 네비게이션 버튼 */
@Composable
fun NavigationButton(iconRes: Int, isSelected: Boolean, onClick: () -> Unit) {
    Icon(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        tint = if (isSelected) Color.White else Color(0xFF63B4F6),
        modifier = Modifier
            .size(40.dp)
            .clickable(onClick = onClick)
    )
}