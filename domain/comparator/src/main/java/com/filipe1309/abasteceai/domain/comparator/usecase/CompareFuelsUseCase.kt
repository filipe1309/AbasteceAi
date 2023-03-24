package com.filipe1309.abasteceai.domain.comparator.usecase

import com.filipe1309.abasteceai.domain.comparator.entity.ComparisonResult

class CompareFuelsUseCase {
    fun invoke(
        fuel1Price: Double, fuel1Efficiency: Double,
        fuel2Price: Double, fuel2Efficiency: Double
    ): ComparisonResult {
        // Explain: (price / efficiency) = cost per unit distance (ex R$/km)
        val fuel1CostPerUnitDistance = fuel1Price / fuel1Efficiency
        val fuel2CostPerUnitDistance = fuel2Price / fuel2Efficiency

        return if (fuel1CostPerUnitDistance < fuel2CostPerUnitDistance) {
            ComparisonResult(true, fuel1CostPerUnitDistance)
        } else {
            ComparisonResult(false, fuel2CostPerUnitDistance)
        }
    }
}
