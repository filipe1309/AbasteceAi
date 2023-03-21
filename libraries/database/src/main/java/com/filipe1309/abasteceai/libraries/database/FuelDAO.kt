package com.filipe1309.abasteceai.libraries.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.filipe1309.abasteceai.libraries.database.model.Fuel
import kotlinx.coroutines.flow.Flow

@Dao
interface FuelDAO {
    @Query("SELECT * FROM fuels")
    fun getAllFuels(): Flow<List<Fuel>>

    @Query("SELECT * FROM fuels WHERE id = :id")
    suspend fun getFuel(id: Int): Fuel

    @Insert
    suspend fun insertFuel(fuel: Fuel): Long
}
