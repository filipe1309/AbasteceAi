package com.filipe1309.abasteceai.features.comparator.domain.entity

data class ComparisonResult(
    val fuel: Fuel = Fuel(0, "", 0.0, 0.0, ""),
    val costPerUnitDistance: Double = 0.0,
)
