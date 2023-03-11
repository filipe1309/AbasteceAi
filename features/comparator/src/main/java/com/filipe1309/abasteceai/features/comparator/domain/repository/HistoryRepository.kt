package com.filipe1309.abasteceai.features.comparator.domain.repository

import com.filipe1309.abasteceai.features.comparator.domain.entity.Fuel

interface HistoryRepository {
    suspend fun saveComparison(fuel1: Fuel, fuel2: Fuel): Boolean
}
