package com.filipe1309.abasteceai.features.comparator.domain.usecase

import com.filipe1309.abasteceai.features.comparator.domain.entity.Fuel
import com.filipe1309.abasteceai.features.comparator.domain.repository.HistoryRepository

class SaveComparisonUseCase(private val repository: HistoryRepository) {
    suspend fun invoke(fuel1: Fuel, fuel2: Fuel): Boolean = repository.saveComparison(fuel1, fuel2)
}
