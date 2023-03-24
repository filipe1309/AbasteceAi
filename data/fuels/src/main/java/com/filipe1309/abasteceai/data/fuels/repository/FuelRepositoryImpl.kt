package com.filipe1309.abasteceai.data.fuels.repository

import com.filipe1309.abasteceai.data.fuels.datasource.FuelLocalDataSource
import com.filipe1309.abasteceai.domain.fuels.entity.Fuel
import com.filipe1309.abasteceai.domain.fuels.repository.FuelRepository
import kotlinx.coroutines.flow.Flow

class FuelRepositoryImpl(
    private val fuelLocalDataSource: FuelLocalDataSource
): FuelRepository {
    override suspend fun getFuel(fuel: Fuel): Fuel = fuelLocalDataSource.getFuel(fuel.id!!).toDomain()

    override suspend fun getFuels(): Flow<List<Fuel>> = fuelLocalDataSource.getAllFuels().toDomain()
}
