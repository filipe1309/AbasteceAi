package com.filipe1309.abasteceai.domain.histories.entity

import java.util.Date

data class History(
    val id: Int? = null,
    val fuelId1: Int,
    val fuelId2: Int,
    val fuelPrice1: Double,
    val fuelPrice2: Double,
    val fuelEfficiency1: Double,
    val fuelEfficiency2: Double,
    val date: Date? = Date(),
    val location: Any? = null
)

