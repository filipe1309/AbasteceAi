package com.filipe1309.abasteceai.features.comparator.presentation

sealed class ComparatorViewIntent {
    object OnSpinnerRendered : ComparatorViewIntent()
    object OnSnackBarRendered : ComparatorViewIntent()
    object OnSaveClicked : ComparatorViewIntent()
    data class OnFuelTyped(val firstFuelPrice: Double, val secondFuelPrice: Double) : ComparatorViewIntent()
    data class OnAddFuelClicked(val isfirstFuel: Boolean) : ComparatorViewIntent()
    data class OnSubtractFuelClicked(val isfirstFuel: Boolean) : ComparatorViewIntent()
    data class OnFuelSelected(val isfirstFuel: Boolean, val position: Int) : ComparatorViewIntent()
}
