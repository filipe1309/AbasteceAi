package com.filipe1309.abasteceai.features.comparator.data.repository

import com.filipe1309.abasteceai.features.comparator.domain.entity.Fuel
import com.filipe1309.abasteceai.features.comparator.domain.repository.HistoryRepository

class HistoryRepositoryImpl: HistoryRepository {
    override suspend fun saveComparison(fuel1: Fuel, fuel2: Fuel): Boolean {
        return true
    }
}
