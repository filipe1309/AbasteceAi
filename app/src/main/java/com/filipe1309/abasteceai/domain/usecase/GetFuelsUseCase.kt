package com.filipe1309.abasteceai.domain.usecase

import com.filipe1309.abasteceai.domain.entity.Fuel
import com.filipe1309.abasteceai.domain.repository.FuelRepository
import kotlinx.coroutines.flow.Flow

class GetFuelsUseCase(private val repository: FuelRepository) {
    suspend fun invoke(): Flow<List<Fuel>> = repository.getFuels()
}