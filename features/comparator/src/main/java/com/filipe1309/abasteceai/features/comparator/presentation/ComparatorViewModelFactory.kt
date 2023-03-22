package com.filipe1309.abasteceai.features.comparator.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.filipe1309.abasteceai.features.comparator.data.repository.FuelRepositoryImpl
import com.filipe1309.abasteceai.features.comparator.data.repository.HistoryRepositoryImpl
import com.filipe1309.abasteceai.features.comparator.data.repository.datasource.FuelLocalDataSourceImpl
import com.filipe1309.abasteceai.features.comparator.data.repository.datasource.HistoryLocalDataSourceImpl
import com.filipe1309.abasteceai.features.comparator.domain.repository.FuelRepository
import com.filipe1309.abasteceai.features.comparator.domain.repository.HistoryRepository
import com.filipe1309.abasteceai.features.comparator.domain.usecase.CompareFuelsUseCase
import com.filipe1309.abasteceai.features.comparator.domain.usecase.GetFuelsUseCase
import com.filipe1309.abasteceai.features.comparator.domain.usecase.SaveComparisonUseCase
import com.filipe1309.abasteceai.libraries.database.AppDatabase

class ComparatorViewModelFactory(
    private val context: Context
 ): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("ViewModelFactory", "create: ")
        val db = AppDatabase.getInstance(context)
        val fuelDao = db.fuelDAO
        val historyDAO = db.historyDAO
        val fuelLocalDataSource = FuelLocalDataSourceImpl(fuelDao)
        val fuelRepository: FuelRepository = FuelRepositoryImpl(fuelLocalDataSource)
        val historyLocalDataSource = HistoryLocalDataSourceImpl(historyDAO)
        val historyRepository: HistoryRepository = HistoryRepositoryImpl(historyLocalDataSource)
        val compareFuelsUseCase = CompareFuelsUseCase()
        val getFuelsUseCase = GetFuelsUseCase(fuelRepository)
        val saveComparisonUseCase = SaveComparisonUseCase(historyRepository)
        val useCasesComparator = UseCasesComparator(compareFuelsUseCase, getFuelsUseCase, saveComparisonUseCase)
        @Suppress("UNCHECKED_CAST")
        return ComparatorViewModel(useCasesComparator) as T
    }
}
