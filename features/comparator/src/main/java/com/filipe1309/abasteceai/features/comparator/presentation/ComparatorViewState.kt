package com.filipe1309.abasteceai.features.comparator.presentation

import com.filipe1309.abasteceai.features.comparator.domain.entity.ComparisonResult
import com.filipe1309.abasteceai.features.comparator.domain.entity.Fuel

data class ComparatorViewState (
    val isLoading: Boolean = false,
    val isComparing: Boolean = false,
    val isFuelsLoaded: Boolean = false,
    val isFuelsReadyToCompare: Boolean = false,
    val error: String? = null,
    val firstFuel: Fuel? = null,
    val secondFuel: Fuel? = null,
    val fuels: List<Fuel>? = null,
    val comparisonResult: ComparisonResult? = null
)
