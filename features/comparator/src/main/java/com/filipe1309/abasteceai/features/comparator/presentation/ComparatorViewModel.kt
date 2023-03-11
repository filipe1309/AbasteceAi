package com.filipe1309.abasteceai.features.comparator.presentation

import android.util.Log
import androidx.lifecycle.*
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

    private var currentViewState = ComparatorViewState(isLoading = true)

    private val _viewState = MutableLiveData<ComparatorViewState>()
    val viewState: LiveData<ComparatorViewState> = _viewState
    init {
        getFuels()
        setState(currentViewState.copy())
    }

    private fun setState(viewState: ComparatorViewState) {
        currentViewState = viewState
        _viewState.postValue(currentViewState)
    }

    fun sendAction(action: ComparatorAction) {
        when (action) {
            is ComparatorAction.GetFuels -> getFuels()
            is ComparatorAction.CompareFuels -> compareFuels()
            is ComparatorAction.SpinnerRendered -> setState(currentViewState.copy(isFuelsReadyToCompare = true))
            is ComparatorAction.FuelSelected -> {
                if (action.isFirstFuel) {
                    setState(currentViewState.copy(firstFuel = currentViewState.fuels?.get(action.fuelPosition)))
                } else {
                    setState(currentViewState.copy(secondFuel = currentViewState.fuels?.get(action.fuelPosition)))
                }
            }
            is ComparatorAction.FuelPriceUpdated -> { compareFuels() }
            is ComparatorAction.ButtonAddFuelClicked -> {
                if (action.isFirstFuel) {
                    currentViewState.firstFuel?.price = currentViewState.firstFuel?.price?.plus(
                        INCREMENT_VALUE
                    )!!
                    currentViewState.firstFuel?.price = String.format(
                        Locale.getDefault(),"%.2f", currentViewState.firstFuel?.price
                    ).toDouble()
                    setState(currentViewState.copy(firstFuel = currentViewState.firstFuel))
                } else {
                    currentViewState.secondFuel?.price = currentViewState.secondFuel?.price?.plus(
                        INCREMENT_VALUE
                    )!!
                    currentViewState.secondFuel?.price = String.format(
                        Locale.getDefault(),"%.2f", currentViewState.secondFuel?.price
                    ).toDouble()
                    setState(currentViewState.copy(secondFuel = currentViewState.secondFuel))
                }
            }
            is ComparatorAction.ButtonRemoveFuelClicked -> {
                if (action.isFirstFuel) {
                    currentViewState.firstFuel?.price = currentViewState.firstFuel?.price?.minus(
                        INCREMENT_VALUE
                    )!!
                    currentViewState.firstFuel?.price = String.format(
                        Locale.getDefault(),"%.2f", currentViewState.firstFuel?.price
                    ).toDouble()
                    setState(currentViewState.copy(firstFuel = currentViewState.firstFuel))
                } else {
                    currentViewState.secondFuel?.price = currentViewState.secondFuel?.price?.minus(
                        INCREMENT_VALUE
                    )!!
                    currentViewState.secondFuel?.price = String.format(
                        Locale.getDefault(),"%.2f", currentViewState.secondFuel?.price
                    ).toDouble()
                    setState(currentViewState.copy(secondFuel = currentViewState.secondFuel))
                }
            }
            is ComparatorAction.ButtonSaveComparisonClicked -> {
                saveComparison()
            }
            is ComparatorAction.SnackBarRendered -> {
                setState(currentViewState.copy(isComparisonSaved = false, isError = false))
            }
        }
    }

    private fun getFuels() {
        Log.d(TAG, "getFuels: ")
        viewModelScope.launch(Dispatchers.IO) {
            useCasesComparator.getFuelsUseCase.invoke().collect { fuels ->
                setState(
                    currentViewState.copy(
                        isLoading = false,
                        isComparing = false,
                        isFuelsLoaded = true,
                        isFuelsReadyToCompare = false,
                        fuels = fuels,
                        firstFuel = fuels[0],
                        secondFuel = fuels[1],
                    )
                )
            }
        }
    }

    private fun compareFuels() {
        Log.d(TAG, "compareFuels: ")
        setState(currentViewState.copy(isLoading = true, isComparing = true))
        viewModelScope.launch(Dispatchers.IO) {
            val comparisonResult = useCasesComparator.compareFuelsUseCase.invoke(
                currentViewState.firstFuel!!, currentViewState.secondFuel!!
            )
            if (comparisonResult.fuel == currentViewState.firstFuel!!) {
                currentViewState.firstFuel?.color = android.R.color.holo_green_light
                currentViewState.secondFuel?.color = android.R.color.holo_red_light
            } else {
                currentViewState.firstFuel?.color = android.R.color.holo_red_light
                currentViewState.secondFuel?.color = android.R.color.holo_green_light
            }
            setState(currentViewState.copy(
                isLoading = false,
                comparisonResult = comparisonResult,
            ))
        }
    }

    private fun saveComparison() {
        Log.d(TAG, "saveComparison: ")
        viewModelScope.launch(Dispatchers.IO) {
            val isComparisonSaved = useCasesComparator.saveComparisonUseCase.invoke(
                currentViewState.firstFuel!!, currentViewState.secondFuel!!
            )

            if (isComparisonSaved) {
                setState(currentViewState.copy(
                    isLoading = false,
                    isComparisonSaved = true,
                    message = R.string.comparison_saved
                ))
            } else {
                setState(currentViewState.copy(
                    isLoading = false,
                    isComparisonSaved = false,
                    isError = true,
                    message = R.string.comparison_not_saved
                ))
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val fuelRepository: FuelRepository = FuelRepositoryImpl()
                val historyRepository: HistoryRepository = HistoryRepositoryImpl()
                val compareFuelsUseCase = CompareFuelsUseCase(fuelRepository)
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
