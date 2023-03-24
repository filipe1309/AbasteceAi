package com.filipe1309.abasteceai.domain.fuels.usecase

import com.filipe1309.abasteceai.domain.fuels.entity.Fuel
import com.filipe1309.abasteceai.domain.fuels.repository.FuelRepository

class UpdateFuelsUseCase(private val repository: FuelRepository) {
    suspend operator fun invoke(vararg fuels: Fuel) = repository.updateFuels(*fuels)
}
