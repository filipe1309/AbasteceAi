package com.filipe1309.abasteceai.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.filipe1309.abasteceai.domain.usecase.CompareFuelsUseCase
import com.filipe1309.abasteceai.domain.usecase.GetFuelsUseCase

class ComparatorViewModelFactory(
    private val compareFuelsUseCase: CompareFuelsUseCase,
    private val getFuelsUseCase: GetFuelsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ComparatorViewModel(compareFuelsUseCase, getFuelsUseCase) as T
    }
}