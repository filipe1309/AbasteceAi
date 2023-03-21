package com.filipe1309.abasteceai.features.comparator.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipe1309.abasteceai.features.comparator.R
import com.filipe1309.abasteceai.features.comparator.domain.usecase.CompareFuelsUseCase
import com.filipe1309.abasteceai.features.comparator.domain.usecase.GetFuelsUseCase
import com.filipe1309.abasteceai.features.comparator.domain.usecase.SaveComparisonUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.absoluteValue

private const val TAG = "ComparatorViewModel"

data class UseCasesComparator(
    val compareFuelsUseCase: CompareFuelsUseCase,
    val getFuelsUseCase: GetFuelsUseCase,
    val saveComparisonUseCase: SaveComparisonUseCase
)

private const val INCREMENT_VALUE = 0.01
private const val INCREMENT_LONG_VALUE = 0.1

class ComparatorViewModel(
    private val useCasesComparator: UseCasesComparator,
): ViewModel() {

    private val _uiState = MutableStateFlow(ComparatorViewState(isLoading = true))
    val uiState: StateFlow<ComparatorViewState> =_uiState.asStateFlow()

    private val _action = MutableSharedFlow<ComparatorAction>()
    val action: SharedFlow<ComparatorAction> =_action.asSharedFlow()


    init {
        setState(ComparatorViewState(isLoading = true))
        viewModelScope.launch(Dispatchers.IO) {
            getFuels()
        }
    }

    fun dispatchViewIntent(viewIntent: ComparatorViewIntent) {
        when (viewIntent) {
            is ComparatorViewIntent.OnFuelSelected -> onFuelSelected(viewIntent)
            is ComparatorViewIntent.OnSpinnerRendered -> onSpinnerRendered()
            is ComparatorViewIntent.OnSnackBarRendered -> onSnackBarRendered()
            is ComparatorViewIntent.OnSaveClicked -> onSaveClicked()
            is ComparatorViewIntent.OnFuelTyped -> onFuelTyped(viewIntent)
            is ComparatorViewIntent.OnAddFuelClicked -> onAddFuelClicked(viewIntent)
            is ComparatorViewIntent.OnSubtractFuelClicked -> onSubtractFuelClicked(viewIntent)
        }
    }

    private fun sendAction(action: ComparatorAction) {
        viewModelScope.launch {
            _action.emit(action)
        }
    }

    private fun setState(uiState: ComparatorViewState) {
        _uiState.value = uiState
    }

    private fun onFuelSelected(viewIntent: ComparatorViewIntent.OnFuelSelected) {
        Log.d(TAG, "onFuelSelected: $viewIntent")
        fuelSelected(viewIntent.isfirstFuel, viewIntent.position)
    }

    private fun onFuelTyped(viewIntent: ComparatorViewIntent.OnFuelTyped) {
        Log.d(TAG, "nnFuelTyped: $viewIntent")
        setState(uiState.value.copy(isLoading = true, isComparing = true))
        compareFuels(viewIntent.firstFuelPrice, viewIntent.secondFuelPrice)
    }

    private fun onSaveClicked() {
        Log.d(TAG, "onSaveClicked")
        setState(uiState.value.copy(isLoading = true))
        saveComparison()
    }

    private fun onAddFuelClicked(viewIntent: ComparatorViewIntent.OnAddFuelClicked) {
        Log.d(TAG, "onAddFuelClicked: $viewIntent")
        if (viewIntent.isfirstFuel) {
            setState(uiState.value.copy(
                firstFuelPrice = updateFuelPrice(uiState.value.firstFuelPrice, viewIntent.isLong)
            ))
        } else {
            setState(uiState.value.copy(
                secondFuelPrice = updateFuelPrice(uiState.value.secondFuelPrice, viewIntent.isLong)
            ))
        }
    }

    private fun onSubtractFuelClicked(viewIntent: ComparatorViewIntent.OnSubtractFuelClicked) {
        Log.d(TAG, "onSubtractFuelClicked: $viewIntent")
        if (viewIntent.isfirstFuel) {
            setState(uiState.value.copy(
                firstFuelPrice = updateFuelPrice(-uiState.value.firstFuelPrice, viewIntent.isLong)
            ))
        } else {
            setState(uiState.value.copy(
                secondFuelPrice = updateFuelPrice(-uiState.value.secondFuelPrice, viewIntent.isLong)
            ))
        }
    }

    private suspend fun getFuels() {
        Log.d(TAG, "getFuels: ")
        useCasesComparator.getFuelsUseCase.invoke()
            .map { fuels ->
                Log.d(TAG, "getFuels: $fuels")
                setState(uiState.value.copy(
                    isLoading = false,
                    fuels = fuels,
                    isFuelsReadyToCompare = true,
                    isFuelsUpdated = true,
                    firstFuelName = fuels.first().name,
                    secondFuelName = fuels[1].name,
                    firstFuelPrice = fuels.first().price,
                    secondFuelPrice = fuels[1].price
                ))
            }
            .catch {
                setState(uiState.value.copy(isLoading = false, isError = true))
                sendAction(ComparatorAction.ShowSnackBar(R.string.error_loading_fuels))
            }
            .launchIn(viewModelScope)
    }

    private fun compareFuels(firstFuelPrice: Double, secondFuelPrice: Double) {
        Log.d(TAG, "compareFuels: ")
        val firstFuel = uiState.value.fuels!!.first { it.name == uiState.value.firstFuelName }
        val firstFuelUpdated = firstFuel.copy(price = firstFuelPrice)
        val secondFuel = uiState.value.fuels!!.first { it.name == uiState.value.secondFuelName }
        val secondFuelUpdated = secondFuel.copy(price = secondFuelPrice)

        viewModelScope.launch(Dispatchers.IO) {
            val comparisonResult = useCasesComparator.compareFuelsUseCase.invoke(
                firstFuelUpdated, secondFuelUpdated
            )

            Log.d(TAG, "compareFuels: result ${comparisonResult}")

            var firstFuelColor = android.R.color.holo_red_light
            var secondFuelColor = android.R.color.holo_green_light

            if (comparisonResult.fuel.name == firstFuel.name) {
                firstFuelColor = android.R.color.holo_green_light
                secondFuelColor = android.R.color.holo_red_light
            }

            setState(uiState.value.copy(
                isLoading = false,
                comparisonResult = comparisonResult,
                isComparing = false,
                isComparisonSaved = false,
                isFuelsUpdated = false,
                firstFuelColor = firstFuelColor,
                secondFuelColor = secondFuelColor,
                firstFuelPrice = firstFuelUpdated.price,
                secondFuelPrice = secondFuelUpdated.price
            ))
        }
    }

    private fun saveComparison() {
        Log.d(TAG, "saveComparison: ")
        val firstFuel = uiState.value.fuels!!.first { it.name == uiState.value.firstFuelName }
        val secondFuel = uiState.value.fuels!!.first { it.name == uiState.value.secondFuelName }
        viewModelScope.launch(Dispatchers.IO) {
            val isComparisonSaved = useCasesComparator.saveComparisonUseCase.invoke(
                firstFuel, secondFuel
            )
            setState(uiState.value.copy(
                isLoading = false,
                isComparisonSaved = isComparisonSaved,
                isError = !isComparisonSaved
            ))
            sendAction(
                ComparatorAction.ShowSnackBar(
                    if (isComparisonSaved) R.string.comparison_saved else R.string.comparison_not_saved
                )
            )
        }
    }

    private fun onSnackBarRendered() {
        setState(uiState.value.copy(isComparisonSaved = false))
    }

    private fun onSpinnerRendered() {
        setState(uiState.value.copy(isFuelsReadyToCompare = true, isFuelsUpdated = false))
    }

    private fun updateFuelPrice(fuelPrice: Double, isLong: Boolean): Double {
        var fuelPriceUpdated = fuelPrice
        fuelPriceUpdated += if (isLong) INCREMENT_LONG_VALUE else INCREMENT_VALUE
        fuelPriceUpdated = String.format(Locale.getDefault(),"%.2f", fuelPriceUpdated.absoluteValue).toDouble()
        return fuelPriceUpdated
    }

    private fun fuelSelected(isFirstFuel: Boolean, position: Int) {
        val fuel = uiState.value.fuels?.get(position)
        if (isFirstFuel) {
            setState(uiState.value.copy(
                firstFuelName = fuel!!.name,
                firstFuelPrice = fuel.price
            ))
        } else {
            setState(uiState.value.copy(
                secondFuelName = fuel!!.name,
                secondFuelPrice = fuel.price
            ))
        }
    }
}
