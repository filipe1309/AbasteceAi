package com.filipe1309.abasteceai.features.comparator.domain.repository

import com.filipe1309.abasteceai.features.comparator.domain.entity.Fuel
import com.filipe1309.abasteceai.features.comparator.domain.entity.Location

interface HistoryRepository {
    suspend fun saveComparison(fuel1: Fuel, fuel2: Fuel, location: Location? = null): Boolean
}
