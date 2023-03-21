package com.filipe1309.abasteceai.features.comparator.domain.repository

import com.filipe1309.abasteceai.libraries.database.model.Fuel
import kotlinx.coroutines.flow.Flow
import com.filipe1309.abasteceai.features.comparator.domain.entity.Fuel as FuelDomain

interface FuelRepository {
    suspend fun getFuel(fuel: Fuel): FuelDomain
    suspend fun getFuels(): Flow<List<FuelDomain>>
}
