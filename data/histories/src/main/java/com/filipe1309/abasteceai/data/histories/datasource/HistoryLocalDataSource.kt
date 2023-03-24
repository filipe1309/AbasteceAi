package com.filipe1309.abasteceai.data.histories.datasource

import com.filipe1309.abasteceai.domain.histories.entity.History

interface HistoryLocalDataSource {
    suspend fun insertHistory(history: History): Long
}
