package com.filipe1309.abasteceai.features.comparator.data.repository

import com.filipe1309.abasteceai.features.comparator.domain.entity.Fuel
import com.filipe1309.abasteceai.features.comparator.domain.repository.FuelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FuelRepositoryImpl: FuelRepository {
    override suspend fun getFuel(fuel: Fuel): Fuel {
        if (fuel.id == 1) {
            return Fuel(
                    id = 1,
                    name = "Gasolina",
                    price = 5.00,
                    efficiency = 10.0,
                    efficiencyUnit = "km/l"
                )
        } else {
            return Fuel(
                id = 2,
                name = "Etanol",
                price = 3.50,
                efficiency = 7.0,
                efficiencyUnit = "km/l"
            )
        }
    }

    override suspend fun getFuels(): Flow<List<Fuel>> = flow {
        emit(listOf(
            Fuel(
                id = 1,
                name = "Gasolina",
                price = 5.00,
                efficiency = 10.0,
                efficiencyUnit = "km/l"
            ),
            Fuel(
                id = 2,
                name = "Etanol",
                price = 3.50,
                efficiency = 7.0,
                efficiencyUnit = "km/l"
            )
        ))
    }


}
