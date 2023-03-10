package com.filipe1309.abasteceai.features.comparator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ComparatorViewModelFactory(
    private val useCasesComparator: UseCasesComparator,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ComparatorViewModel(useCasesComparator) as T
    }
}
