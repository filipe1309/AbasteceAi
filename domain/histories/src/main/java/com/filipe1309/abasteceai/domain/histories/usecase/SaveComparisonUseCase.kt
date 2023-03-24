package com.filipe1309.abasteceai.domain.histories.usecase

import com.filipe1309.abasteceai.domain.histories.entity.History
import com.filipe1309.abasteceai.domain.histories.repository.HistoryRepository

class SaveComparisonUseCase(private val repository: HistoryRepository) {
    suspend operator fun invoke(history: History): Boolean =
        repository.saveComparison(history)
}
