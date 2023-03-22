package com.filipe1309.abasteceai.features.comparator.data.repository

import com.filipe1309.abasteceai.features.comparator.data.repository.datasource.HistoryLocalDataSource
import com.filipe1309.abasteceai.features.comparator.domain.entity.Fuel
import com.filipe1309.abasteceai.features.comparator.domain.entity.Location
import com.filipe1309.abasteceai.features.comparator.domain.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val historyLocalDataSource: HistoryLocalDataSource
): HistoryRepository {
    override suspend fun saveComparison(fuel1: Fuel, fuel2: Fuel, location: Location?): Boolean {
        return historyLocalDataSource.insertHistory(fuel1.toData(), fuel2.toData(), location) > 0
    }
}
