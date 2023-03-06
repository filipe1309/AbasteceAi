package com.filipe1309.abasteceai.features.comparator.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.filipe1309.abasteceai.features.comparator.domain.entity.ComparisonResult
import com.filipe1309.abasteceai.features.comparator.domain.entity.Fuel
import com.filipe1309.abasteceai.features.comparator.domain.usecase.CompareFuelsUseCase
import com.filipe1309.abasteceai.features.comparator.domain.usecase.GetFuelsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "ComparatorViewModel"

class ComparatorViewModel(
    private val compareFuelsUseCase: CompareFuelsUseCase,
    private val getFuelsUseCase: GetFuelsUseCase
): ViewModel() {
    val firstFuel: MutableLiveData<Fuel> = MutableLiveData()
    val secondFuel: MutableLiveData<Fuel> = MutableLiveData()
    val result: MutableLiveData<ComparisonResult> = MutableLiveData()

    fun compareFuels() = viewModelScope.launch(Dispatchers.IO) {
        if (firstFuel.value == null || secondFuel.value == null) {
            Log.d(TAG, "No fuels selected")
            return@launch
        }

        Log.d(TAG, "Comparing fuels ${firstFuel.value} and ${secondFuel.value}")
        val comparisonResult = compareFuelsUseCase.invoke(firstFuel.value!!, secondFuel.value!!)
        result.postValue(comparisonResult)
    }

    fun getFuels() = liveData {
        getFuelsUseCase.invoke().collect() {
            emit(it)
        }
    }
}
