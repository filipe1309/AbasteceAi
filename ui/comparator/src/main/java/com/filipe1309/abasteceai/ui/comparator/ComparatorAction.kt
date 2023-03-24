package com.filipe1309.abasteceai.ui.comparator

sealed class ComparatorAction {
    data class ShowSnackBar(val message: Int) : ComparatorAction()
}
