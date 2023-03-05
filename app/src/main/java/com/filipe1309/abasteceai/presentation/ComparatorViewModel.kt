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
    val firstFuel: MutableLiveData<Fuel> = MutableLiveData()
    val secondFuel: MutableLiveData<Fuel> = MutableLiveData()
    val result: MutableLiveData<ComparisonResult> = MutableLiveData()

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