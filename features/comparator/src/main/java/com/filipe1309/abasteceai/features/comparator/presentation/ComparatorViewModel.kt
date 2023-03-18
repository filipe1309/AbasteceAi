package com.filipe1309.abasteceai.features.comparator.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.filipe1309.abasteceai.features.comparator.R
import com.filipe1309.abasteceai.features.comparator.data.repository.FuelRepositoryImpl
import com.filipe1309.abasteceai.features.comparator.data.repository.HistoryRepositoryImpl
import com.filipe1309.abasteceai.features.comparator.domain.repository.FuelRepository
import com.filipe1309.abasteceai.features.comparator.domain.repository.HistoryRepository
import com.filipe1309.abasteceai.features.comparator.domain.usecase.CompareFuelsUseCase
import com.filipe1309.abasteceai.features.comparator.domain.usecase.GetFuelsUseCase
import com.filipe1309.abasteceai.features.comparator.domain.usecase.SaveComparisonUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "ComparatorViewModel"

data class UseCasesComparator(
    val compareFuelsUseCase: CompareFuelsUseCase,
    val getFuelsUseCase: GetFuelsUseCase,
    val saveComparisonUseCase: SaveComparisonUseCase
)

private const val INCREMENT_VALUE = 0.01

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

    private fun sendAction(action: ComparatorAction) {
        viewModelScope.launch {
            _action.emit(action)
        }
    }

    private fun setState(uiState: ComparatorViewState) {
        _uiState.value = uiState
    }

    private suspend fun getFuels() {
        Log.d(TAG, "getFuels: ")
        useCasesComparator.getFuelsUseCase.invoke()
            .map { fuels ->
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

    fun compareFuels(firstFuelPrice: Double, secondFuelPrice: Double) {
        Log.d(TAG, "compareFuels: ")
        setState(uiState.value.copy(isLoading = true, isComparing = true))

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

    fun saveComparison() {
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

    fun snackBarRendered() {
        setState(uiState.value.copy(isComparisonSaved = false))
    }

    fun spinnerRendered() {
        setState(uiState.value.copy(isFuelsReadyToCompare = true, isFuelsUpdated = false))
    }

    fun incrementDecrementFuelPrice(isFirstFuel: Boolean, isIncrement: Boolean) {
        Log.d(TAG, "incrementDecrementFuelPrice: isFirstFuel $isFirstFuel, isIncrement $isIncrement")
        if (isFirstFuel) {
            setState(uiState.value.copy(firstFuelPrice = updateFuelPrice(uiState.value.firstFuelPrice, isIncrement)))
        } else {
            setState(uiState.value.copy(secondFuelPrice = updateFuelPrice(uiState.value.secondFuelPrice, isIncrement)))
        }
    }

    private fun updateFuelPrice(fuelPrice: Double, isIncrement: Boolean): Double {
        var fuelPriceUpdated = fuelPrice
        if (isIncrement) {
            fuelPriceUpdated += INCREMENT_VALUE
        } else {
            fuelPriceUpdated -= INCREMENT_VALUE
        }
        fuelPriceUpdated = String.format(Locale.getDefault(),"%.2f", fuelPriceUpdated).toDouble()
        return fuelPriceUpdated
    }

    fun fuelSelected(position: Int, isFirstFuel: Boolean) {
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val fuelRepository: FuelRepository = FuelRepositoryImpl()
                val historyRepository: HistoryRepository = HistoryRepositoryImpl()
                val compareFuelsUseCase = CompareFuelsUseCase()
                val getFuelsUseCase = GetFuelsUseCase(fuelRepository)
                val saveComparisonUseCase = SaveComparisonUseCase(historyRepository)
                val useCasesComparator = UseCasesComparator(compareFuelsUseCase, getFuelsUseCase, saveComparisonUseCase)

                ComparatorViewModel(
                    useCasesComparator = useCasesComparator
                )
            }
        }
    }
}
