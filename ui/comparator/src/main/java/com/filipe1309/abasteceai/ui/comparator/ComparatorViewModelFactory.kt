package com.filipe1309.abasteceai.ui.comparator

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.filipe1309.abasteceai.data.fuels.datasource.FuelLocalDataSourceImpl
import com.filipe1309.abasteceai.data.fuels.repository.FuelRepositoryImpl
import com.filipe1309.abasteceai.data.histories.datasource.HistoryLocalDataSourceImpl
import com.filipe1309.abasteceai.data.histories.repository.HistoryRepositoryImpl
import com.filipe1309.abasteceai.domain.comparator.usecase.CompareFuelsUseCase
import com.filipe1309.abasteceai.domain.fuels.repository.FuelRepository
import com.filipe1309.abasteceai.domain.fuels.usecase.GetFuelsUseCase
import com.filipe1309.abasteceai.domain.fuels.usecase.UpdateFuelsUseCase
import com.filipe1309.abasteceai.domain.histories.repository.HistoryRepository
import com.filipe1309.abasteceai.domain.histories.usecase.SaveComparisonUseCase
import com.filipe1309.abasteceai.libraries.database.AppDatabase

class ComparatorViewModelFactory(
    private val context: Context
 ): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // TODO: Remove after implementing DI
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
        val updateFuelsUseCase = UpdateFuelsUseCase(fuelRepository)
        val saveComparisonUseCase = SaveComparisonUseCase(historyRepository)
        val useCasesComparator = UseCasesComparator(
            compareFuelsUseCase,
            getFuelsUseCase,
            saveComparisonUseCase,
            updateFuelsUseCase
        )
        @Suppress("UNCHECKED_CAST")
        return ComparatorViewModel(useCasesComparator) as T
    }
}
