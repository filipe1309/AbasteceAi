package com.filipe1309.abasteceai.ui.comparator

import com.filipe1309.abasteceai.domain.histories.entity.Location


sealed class ComparatorViewIntent {
    object OnSpinnerRendered : ComparatorViewIntent()
    object OnSnackBarRendered : ComparatorViewIntent()
    object OnSaveClicked : ComparatorViewIntent()
    data class OnFuelChanged(val firstFuelPrice: Double, val secondFuelPrice: Double) : ComparatorViewIntent()
    data class OnAddFuelClicked(val isfirstFuel: Boolean, val isLong: Boolean) : ComparatorViewIntent()
    data class OnSubtractFuelClicked(val isfirstFuel: Boolean, val isLong: Boolean) : ComparatorViewIntent()
    data class OnFuelSelected(val isfirstFuel: Boolean, val position: Int) : ComparatorViewIntent()
    data class OnLocationReceived(val location: Location) : ComparatorViewIntent()
}
