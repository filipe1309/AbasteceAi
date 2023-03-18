package com.filipe1309.abasteceai.features.comparator.presentation

import com.filipe1309.abasteceai.features.comparator.domain.entity.ComparisonResult
import com.filipe1309.abasteceai.features.comparator.domain.entity.Fuel

data class ComparatorViewState (
    val isLoading: Boolean = false,
    val isComparing: Boolean = false,
    val isComparisonSaved: Boolean = false,
    val isFuelsLoaded: Boolean = false,
    val isFuelsReadyToCompare: Boolean = false,
    val isError: Boolean = false,
    val isFuelsUpdated: Boolean = false,
    val firstFuelName: String = "",
    val secondFuelName: String = "",
    val firstFuelPrice: Double = 0.0,
    val secondFuelPrice: Double = 0.0,
    val firstFuelColor: Int = android.R.color.holo_blue_dark,
    val secondFuelColor: Int = android.R.color.holo_blue_dark,
    val fuels: List<Fuel>? = null,
    val comparisonResult: ComparisonResult? = null
)
