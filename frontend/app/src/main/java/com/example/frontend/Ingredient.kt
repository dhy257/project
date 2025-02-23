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
    var state: IngredientState = IngredientState.FRESH
) {

    fun getDday(): Long {
        return ChronoUnit.DAYS.between(addedDate, expiryDate)
    }

    fun waste() {
        state = IngredientState.WASTED // 낭비 상태로 변경
    }

    fun consume() {
        state = IngredientState.CONSUMED // 소비 상태로 변경

    }
}