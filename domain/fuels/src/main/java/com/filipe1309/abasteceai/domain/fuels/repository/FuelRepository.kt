package com.filipe1309.abasteceai.domain.fuels.repository

import com.filipe1309.abasteceai.domain.fuels.entity.Fuel
import kotlinx.coroutines.flow.Flow

interface FuelRepository {
    suspend fun getFuel(fuel: Fuel): Fuel
    suspend fun getFuels(): Flow<List<Fuel>>
    suspend fun updateFuels(vararg fuels: Fuel)
}
