package com.filipe1309.abasteceai.domain.histories.repository

import com.filipe1309.abasteceai.domain.histories.entity.History

interface HistoryRepository {
    suspend fun saveComparison(history: History): Boolean
}
