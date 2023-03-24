package com.filipe1309.abasteceai.data.histories.repository

import com.filipe1309.abasteceai.data.histories.datasource.HistoryLocalDataSource
import com.filipe1309.abasteceai.domain.histories.entity.History
import com.filipe1309.abasteceai.domain.histories.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val historyLocalDataSource: HistoryLocalDataSource
): HistoryRepository {
    override suspend fun saveComparison(history: History): Boolean {
        return historyLocalDataSource.insertHistory(history) > 0
    }
}
