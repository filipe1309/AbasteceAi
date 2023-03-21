package com.filipe1309.abasteceai.libraries.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fuels")
data class Fuel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val price: Double,
    val efficiency: Double,
    @ColumnInfo(name = "efficiency_unit")
    val efficiencyUnit: String
)


