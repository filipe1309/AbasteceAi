package com.filipe1309.abasteceai.features.comparator.data.repository

import com.filipe1309.abasteceai.features.comparator.data.repository.datasource.FuelLocalDataSource
import com.filipe1309.abasteceai.features.comparator.domain.repository.FuelRepository
import com.filipe1309.abasteceai.libraries.database.model.Fuel
import kotlinx.coroutines.flow.Flow
import com.filipe1309.abasteceai.features.comparator.domain.entity.Fuel as FuelDomain

class FuelRepositoryImpl(
    private val fuelLocalDataSource: FuelLocalDataSource
): FuelRepository {
    override suspend fun getFuel(fuel: Fuel): FuelDomain = fuelLocalDataSource.getFuel(fuel.id).toDomain()

    override suspend fun getFuels(): Flow<List<FuelDomain>> = fuelLocalDataSource.getAllFuels().toDomain()
}
