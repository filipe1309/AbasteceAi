package com.filipe1309.abasteceai.domain.usecase

import com.filipe1309.abasteceai.domain.entity.ComparisonResult
import com.filipe1309.abasteceai.domain.entity.Fuel
import com.filipe1309.abasteceai.domain.repository.FuelRepository

class CompareFuelsUseCase(private val repository: FuelRepository) {
    suspend fun invoke(fuel1: Fuel, fuel2: Fuel): ComparisonResult {
        val fuel1Data = repository.getFuel(fuel1)
        val fuel2Data = repository.getFuel(fuel2)

        // Explain: (price / efficiency) = cost per unit distance (ex R$/km)
        val fuel1CostPerUnitDistance = fuel1.price / fuel1Data.efficiency
        val fuel2CostPerUnitDistance = fuel2.price / fuel2Data.efficiency

        return if (fuel1CostPerUnitDistance < fuel2CostPerUnitDistance) {
            ComparisonResult(fuel1, fuel1CostPerUnitDistance)
        } else {
            ComparisonResult(fuel2, fuel2CostPerUnitDistance)
        }
    }
}