package com.example.frontend

import java.time.LocalDate
import java.time.temporal.ChronoUnit

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

//enum class IngredientStatus {
//    FRESH, CONSUMED, DISCARDED
//}
//
//data class Ingredient(
//    val name: String,
//    val addedDate: LocalDate,
//    val expiryDate: LocalDate,
//    val category: String,
//    var status: IngredientStatus = IngredientStatus.FRESH
//) {
//    fun getDday(): Int = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate).toInt()
//}