package com.filipe1309.abasteceai.features.comparator.data.repository.datasource

import com.filipe1309.abasteceai.features.comparator.domain.entity.Location
import com.filipe1309.abasteceai.libraries.database.model.Fuel


interface HistoryLocalDataSource {
    suspend fun insertHistory(fuel1: Fuel, fuel2: Fuel, location: Location? = null): Long
}
