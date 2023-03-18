package com.filipe1309.abasteceai.features.comparator.domain.usecase

import com.filipe1309.abasteceai.features.comparator.domain.entity.ComparisonResult
import com.filipe1309.abasteceai.features.comparator.domain.entity.Fuel

class CompareFuelsUseCase {
    fun invoke(fuel1: Fuel, fuel2: Fuel): ComparisonResult {
        // Explain: (price / efficiency) = cost per unit distance (ex R$/km)
        val fuel1CostPerUnitDistance = fuel1.price / fuel1.efficiency
        val fuel2CostPerUnitDistance = fuel2.price / fuel2.efficiency

        return if (fuel1CostPerUnitDistance < fuel2CostPerUnitDistance) {
            ComparisonResult(fuel1, fuel1CostPerUnitDistance)
        } else {
            ComparisonResult(fuel2, fuel2CostPerUnitDistance)
        }
    }
}
