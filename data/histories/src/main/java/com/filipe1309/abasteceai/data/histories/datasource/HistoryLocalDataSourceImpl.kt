package com.filipe1309.abasteceai.data.histories.datasource

import android.util.Log
import com.filipe1309.abasteceai.domain.histories.entity.History
import com.filipe1309.abasteceai.libraries.database.HistoryDAO
import com.filipe1309.abasteceai.libraries.database.model.History as HistoryModel

class HistoryLocalDataSourceImpl(
    private val historyDAO: HistoryDAO
): HistoryLocalDataSource {
    override suspend fun insertHistory(history: History): Long {
        val historyModel = HistoryModel(
            fuelId1 = history.fuelId1,
            fuelId2 = history.fuelId2,
            fuelPrice1 = history.fuelPrice1,
            fuelPrice2 = history.fuelPrice2,
            fuelEfficiency1 = history.fuelEfficiency1,
            fuelEfficiency2 = history.fuelEfficiency2,
            location =  history.location
        )
        Log.d("HistoryLocalDataSource", "insertHistory: $historyModel")
        return historyDAO.insertHistory(historyModel)
    }
}
