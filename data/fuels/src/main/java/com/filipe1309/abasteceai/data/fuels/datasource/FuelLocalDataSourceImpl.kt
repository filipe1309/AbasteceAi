package com.filipe1309.abasteceai.data.fuels.datasource

import com.filipe1309.abasteceai.libraries.database.FuelDAO
import com.filipe1309.abasteceai.libraries.database.model.Fuel
import kotlinx.coroutines.flow.Flow

class FuelLocalDataSourceImpl(
    private val fuelDAO: FuelDAO
): FuelLocalDataSource {
    override suspend fun getFuel(id: Int): Fuel {
        return fuelDAO.getFuel(id)
    }

    override fun getAllFuels(): Flow<List<Fuel>> {
        return fuelDAO.getAllFuels()
    }
}
