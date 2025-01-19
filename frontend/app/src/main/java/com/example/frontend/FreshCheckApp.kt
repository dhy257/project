package com.example.frontend

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
//
//enum class composetest(){
//    LoginScreen,
//    Register
//}

@Composable
fun FreshCheckApp(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.LoginScreen, builder = {
        composable(Routes.LoginScreen,){
            LoginScreen(navController)
        }
        composable(Routes.RegisterScreen){
            RegisterScreen(navController)
        }
    })
}

//val navController = rememberNavController()
//NavHost(navController = navController, startDestination = Routes.LoginScreen, builder = {
//    composable(Routes.LoginScreen,){
//        LoginScreen(navController)
//    }
//    composable(Routes.RegisterScreen){
//        RegisterScreen()
//    }
//})