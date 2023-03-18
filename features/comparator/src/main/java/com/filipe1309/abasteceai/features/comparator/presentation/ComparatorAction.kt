package com.filipe1309.abasteceai.features.comparator.presentation

sealed class ComparatorAction {
    data class ShowSnackBar(val message: Int) : ComparatorAction()
}
