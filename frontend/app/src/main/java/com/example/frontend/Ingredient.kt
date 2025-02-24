package com.example.frontend

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.UUID

enum class IngredientState {
    FRESH, CONSUMED, WASTED
}

data class Ingredient(
    val id: String = UUID.randomUUID().toString(),  // 고유 ID 자동 생성
    val name: String,
    val category: String,
    val addedDate: LocalDate,
    val expiryDate: LocalDate,
    var state: IngredientState = IngredientState.FRESH,
    var stateChangeDate: LocalDate? = null // 상태 변경 날짜 추가
) {

    fun getDday(): Long {
        return ChronoUnit.DAYS.between(addedDate, expiryDate)
    }

    fun waste() {
        state = IngredientState.WASTED // 낭비 상태로 변경
        stateChangeDate = LocalDate.now() // 낭비 상태 변경 날짜 기록
    }

    fun consume() {
        state = IngredientState.CONSUMED // 소비 상태로 변경
        stateChangeDate = LocalDate.now() // 낭비 상태 변경 날짜 기록
    }
}