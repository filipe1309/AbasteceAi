package com.filipe1309.abasteceai.features.comparator.presentation

sealed class ComparatorAction {
    object GetFuels : ComparatorAction()
    object SpinnerRendered : ComparatorAction()
    data class FuelSelected(val fuelPosition: Int, val isFirstFuel: Boolean) : ComparatorAction()
    object FuelPriceUpdated : ComparatorAction()
    data class CompareFuels(val firstFuelPosition: Int, val secondFuelPosition: Int) : ComparatorAction()
    data class ButtonAddFuelClicked(val isFirstFuel: Boolean) : ComparatorAction()
    data class ButtonRemoveFuelClicked(val isFirstFuel: Boolean) : ComparatorAction()
    object ButtonSaveComparisonClicked : ComparatorAction()
    object SnackBarRendered : ComparatorAction()
}
