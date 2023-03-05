package com.filipe1309.abasteceai.domain.entity

data class Fuel(
    val id: Int,
    val name: String,
    var price: Double,
    val efficiency: Double,
    val efficiencyUnit: String
)
