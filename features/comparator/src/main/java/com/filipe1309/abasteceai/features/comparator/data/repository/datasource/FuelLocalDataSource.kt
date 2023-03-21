package com.filipe1309.abasteceai.features.comparator.data.repository.datasource

import com.filipe1309.abasteceai.libraries.database.model.Fuel
import kotlinx.coroutines.flow.Flow

interface FuelLocalDataSource {
    fun getAllFuels(): Flow<List<Fuel>>
    suspend fun getFuel(id: Int): Fuel
}
