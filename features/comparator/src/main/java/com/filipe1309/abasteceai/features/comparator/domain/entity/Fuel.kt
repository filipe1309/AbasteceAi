package com.filipe1309.abasteceai.features.comparator.domain.entity

data class Fuel(
    val id: Int? = null,
    val name: String,
    var price: Double,
    val efficiency: Double,
    val efficiencyUnit: String,
    var color: Int = 0
)
