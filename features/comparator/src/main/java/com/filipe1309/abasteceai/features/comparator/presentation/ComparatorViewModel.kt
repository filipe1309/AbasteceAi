package com.filipe1309.abasteceai.features.comparator.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipe1309.abasteceai.features.comparator.domain.usecase.CompareFuelsUseCase
import com.filipe1309.abasteceai.features.comparator.domain.usecase.GetFuelsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "ComparatorViewModel"

data class UseCasesComparator(
    val compareFuelsUseCase: CompareFuelsUseCase,
    val getFuelsUseCase: GetFuelsUseCase
)

class ComparatorViewModel(
    private val useCasesComparator: UseCasesComparator,
): ViewModel() {

    private var currentViewState = ComparatorViewState()

    private val _viewState = MutableLiveData<ComparatorViewState>()
    val viewState: LiveData<ComparatorViewState> = _viewState
    init {
        getFuels()
        setState(
            ComparatorViewState(
                isLoading = true,
                isComparing = false,
                isFuelsLoaded = false,
                isFuelsReadyToCompare = false,
                error = null,
                firstFuel = null,
                secondFuel = null,
                fuels = null,
                comparisonResult = null
            )
        )
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
}
