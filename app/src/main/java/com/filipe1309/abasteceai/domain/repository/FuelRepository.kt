package com.filipe1309.abasteceai.domain.repository

import com.filipe1309.abasteceai.domain.entity.Fuel
import kotlinx.coroutines.flow.Flow

interface FuelRepository {
    suspend fun getFuel(fuel: Fuel): Fuel
    suspend fun getFuels(): Flow<List<Fuel>>
}