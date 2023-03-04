package com.filipe1309.abasteceai.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.filipe1309.abasteceai.domain.entity.ComparisonResult
import com.filipe1309.abasteceai.domain.entity.Fuel
import com.filipe1309.abasteceai.domain.usecase.CompareFuelsUseCase
import com.filipe1309.abasteceai.domain.usecase.GetFuelsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComparatorViewModel(
    private val compareFuelsUseCase: CompareFuelsUseCase,
    private val getFuelsUseCase: GetFuelsUseCase
): ViewModel() {
    var firstFuel: MutableLiveData<Fuel> = MutableLiveData()
    var secondFuel: MutableLiveData<Fuel> = MutableLiveData()
    var result: MutableLiveData<ComparisonResult> = MutableLiveData()

    fun setFirstFuelPrice(price: Double) {
        firstFuel.value?.price = price
    }

    fun setSecondFuelPrice(price: Double) {
        secondFuel.value?.price = price
    }

    fun compareFuels() = viewModelScope.launch(Dispatchers.IO) {
        val comparisonResult = compareFuelsUseCase.invoke(firstFuel.value!!, secondFuel.value!!)
        result.postValue(comparisonResult)
    }

    fun getFuels() = liveData {
        getFuelsUseCase.invoke().collect() {
            emit(it)
        }
    }
}