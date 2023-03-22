package com.filipe1309.abasteceai.features.comparator.data.repository.datasource

import com.filipe1309.abasteceai.libraries.database.HistoryDAO
import com.filipe1309.abasteceai.libraries.database.model.Fuel
import com.filipe1309.abasteceai.libraries.database.model.History

class HistoryLocalDataSourceImpl(
    private val historyDAO: HistoryDAO
): HistoryLocalDataSource {
    override suspend fun insertHistory(fuel1: Fuel, fuel2: Fuel): Long {

        val history = History(
            fuelId1 = fuel1.id,
            fuelId2 = fuel2.id,
            fuelPrice1 = fuel1.price,
            fuelPrice2 = fuel2.price,
            fuelEfficiency1 = fuel1.efficiency,
            fuelEfficiency2 = fuel2.efficiency
        )
        return historyDAO.insertHistory(history)
    }
}
