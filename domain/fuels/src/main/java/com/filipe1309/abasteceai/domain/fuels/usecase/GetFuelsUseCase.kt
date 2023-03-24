package com.filipe1309.abasteceai.domain.fuels.usecase

import com.filipe1309.abasteceai.domain.fuels.entity.Fuel
import com.filipe1309.abasteceai.domain.fuels.repository.FuelRepository
import kotlinx.coroutines.flow.Flow

class GetFuelsUseCase(private val repository: FuelRepository) {
    suspend fun invoke(): Flow<List<Fuel>> = repository.getFuels()
}
