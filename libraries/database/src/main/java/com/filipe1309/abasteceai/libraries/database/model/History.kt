package com.filipe1309.abasteceai.libraries.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "histories")
data class History(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "fuel_id_1")
    val fuelId1: Int,
    @ColumnInfo(name = "fuel_id_2")
    val fuelId2: Int,
    @ColumnInfo(name = "fuel_price_1")
    val fuelPrice1: Double,
    @ColumnInfo(name = "fuel_price_2")
    val fuelPrice2: Double,
    @ColumnInfo(name = "fuel_efficiency_1")
    val fuelEfficiency1: Double,
    @ColumnInfo(name = "fuel_efficiency_2")
    val fuelEfficiency2: Double,
    @ColumnInfo(name = "timestamp")
    val date: Date? = Date(),
    @ColumnInfo(name = "geolocation")
    val location: Any? = null
)

