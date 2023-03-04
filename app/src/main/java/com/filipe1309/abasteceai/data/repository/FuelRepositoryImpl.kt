package com.filipe1309.abasteceai.data.repository

import com.filipe1309.abasteceai.domain.entity.Fuel
import com.filipe1309.abasteceai.domain.repository.FuelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FuelRepositoryImpl: FuelRepository {
    override suspend fun getFuel(fuel: Fuel): Fuel {
        if (fuel.id == 1) {
            return Fuel(
                    id = 1,
                    name = "Gasolina",
                    price = 4.99,
                    efficiency = 10.0
                )
        } else {
            return Fuel(
                id = 2,
                name = "Etanol",
                price = 3.99,
                efficiency = 8.0
            )
        }
        // TODO: return FuelLocalDataSource.getFuel(fuel)
    }

    override suspend fun getFuels(): Flow<List<Fuel>> = flow {
        emit(listOf(
            Fuel(
                id = 1,
                name = "Gasolina",
                price = 4.99,
                efficiency = 10.0
            ),
            Fuel(
                id = 2,
                name = "Etanol",
                price = 3.99,
                efficiency = 8.0
            )
        ))
    }


}