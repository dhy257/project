package com.example.frontend


data class CategoryScore(
    val category: String,
    val totalItems: Int,
    val consumedItems: Int,
    val wastedItems: Int,
    val freshItems: Int
) {
    val score: Int
        get() = if (totalItems > 0) (consumedItems * 100) / totalItems else 0
}

// **카테고리별 점수 분석**
fun calculateCategoryScores(ingredientList: List<Ingredient>): List<CategoryScore> {
    val categoryGroups = ingredientList.groupBy { it.category }

    return categoryGroups.map { (category, ingredients) ->
        val consumed = ingredients.count { it.state == IngredientState.CONSUMED }
        val wasted = ingredients.count { it.state == IngredientState.WASTED }
        val fresh = ingredients.count { it.state == IngredientState.FRESH }
        val total = ingredients.size

        CategoryScore(
            category = category,
            totalItems = total,
            consumedItems = consumed,
            wastedItems = wasted,
            freshItems = fresh
        )
    }.sortedByDescending { it.score } // 점수 높은 순 정렬
}