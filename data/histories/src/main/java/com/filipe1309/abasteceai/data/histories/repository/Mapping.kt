package com.filipe1309.abasteceai.data.histories.repository

import com.filipe1309.abasteceai.libraries.database.model.Fuel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.filipe1309.abasteceai.domain.fuels.entity.Fuel as FuelDomain

internal fun Fuel.toDomain(): FuelDomain {
    return FuelDomain(
        id = this.id,
        name = this.name,
        price = this.price,
        efficiency = this.efficiency,
        efficiencyUnit = this.efficiencyUnit
    )
}

internal fun FuelDomain.toData(): Fuel {
    return Fuel(
        id = this.id ?: 0,
        name = this.name,
        price = this.price,
        efficiency = this.efficiency,
        efficiencyUnit = this.efficiencyUnit
    )
}

internal fun Flow<List<Fuel>>.toDomain(): Flow<List<FuelDomain>> {
    return this.map { list ->
        list.map { it.toDomain() }
    }
}
