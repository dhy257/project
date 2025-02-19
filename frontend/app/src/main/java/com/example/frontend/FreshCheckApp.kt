package com.example.frontend

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun FreshCheckApp() {
    val navController = rememberNavController()
    val context = LocalContext.current  // Context 가져오기
    NavHost(navController = navController,
        startDestination = Routes.LoginScreen,
        builder = {
            //로그인 화면
            composable(Routes.LoginScreen) {
                LoginScreen(navController, context)
            }
            //회원가입 화면
            composable(Routes.RegisterScreen) {
                RegisterScreen(navController, context)
            }

            //메인화면
            composable(Routes.MainScreen) {
                MainScreen(navController)
            }

            //캘린더 화면
            composable(Routes.CalendarScreen) {
                CalendarScreen(navController)
            }

            //재료추가 화면
            composable(Routes.IngredientScreen) {
                IngredientScreen(navController)
            }

            //영수증 보기 화면
            composable(Routes.ReciptScreen) {
                ReciptScreen(navController)
            }



        })
}
