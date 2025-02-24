package com.example.frontend.extrafunc

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontend.AnalysisScreen
import com.example.frontend.CalendarScreen
import com.example.frontend.CameraScreen
import com.example.frontend.FridgeScreen
import com.example.frontend.HistoryListScreen
import com.example.frontend.IngredientScreen
import com.example.frontend.LoginScreen
import com.example.frontend.MainScreen
import com.example.frontend.ReciptScreen
import com.example.frontend.RegisterScreen
import com.example.frontend.Routes
import com.example.frontend.SettingScreen
import com.example.frontend.historyListState


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

            //카메라 화면
            composable(Routes.CameraScreen) {
                CameraScreen(navController)
            }

            //냉장고 화면
            composable(Routes.FridgeScreen) {
                FridgeScreen(navController)
            }

            //소비/낭비 내역 추가
            composable(Routes.HistoryListScreen) {
                HistoryListScreen(navController, historyListState)
            }

            // 분석화면 추가
            composable(Routes.AnalysisScreen) {
                AnalysisScreen(navController)
            }

            // 설정 추가
            composable(Routes.SettingScreen) {
                SettingScreen(navController)
            }

        })
}
