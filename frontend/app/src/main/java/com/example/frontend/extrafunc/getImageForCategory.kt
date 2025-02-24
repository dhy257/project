package com.example.frontend

import androidx.compose.runtime.Composable

@Composable
fun getImageForCategory(category: String): Int {
    return when (category) {
        "즉석식품" -> R.drawable.alarm_icon
        "음료" -> R.drawable.back_arrow
        "가공식품" -> R.drawable.email_icon
        "조미식품" -> R.drawable.password_icon
        "유제품" -> R.drawable.analysis_icon
        "신선식품" -> R.drawable.calendar_icon
        "어패류" -> R.drawable.camera_icon
        "육류" -> R.drawable.search_icon
        else -> R.drawable.name_icon // 기본 아이콘
    }
}