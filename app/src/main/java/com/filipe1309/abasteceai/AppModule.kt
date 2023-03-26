package com.filipe1309.abasteceai

import com.filipe1309.abasteceai.data.fuels.datasource.FuelLocalDataSource
import com.filipe1309.abasteceai.data.fuels.datasource.FuelLocalDataSourceImpl
import com.filipe1309.abasteceai.data.fuels.repository.FuelRepositoryImpl
import com.filipe1309.abasteceai.data.histories.datasource.HistoryLocalDataSource
import com.filipe1309.abasteceai.data.histories.datasource.HistoryLocalDataSourceImpl
import com.filipe1309.abasteceai.data.histories.repository.HistoryRepositoryImpl
import com.filipe1309.abasteceai.domain.fuels.repository.FuelRepository
import com.filipe1309.abasteceai.domain.histories.repository.HistoryRepository
import com.filipe1309.abasteceai.libraries.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
//    // ViewModels
//    viewModel { ComparatorViewModel(get()) }
//
//    // Use cases
//    factory { CompareFuelsUseCase() }
//    factory { GetFuelsUseCase(get()) }
//    factory { SaveComparisonUseCase(get()) }
//    factory { UpdateFuelsUseCase(get()) }
//    factory { UseCasesComparator(get(), get(), get(), get() ) }

    // Repositories
    factory<FuelRepository> { FuelRepositoryImpl(get()) }
    factory<HistoryRepository> { HistoryRepositoryImpl(get()) }

    // Data sources
    factory<FuelLocalDataSource> { FuelLocalDataSourceImpl(fuelDAO = get()) }
    factory<HistoryLocalDataSource> { HistoryLocalDataSourceImpl(historyDAO = get()) }

    // DAOs
    factory { get<AppDatabase>().fuelDAO }
    factory { get<AppDatabase>().historyDAO }

    // Database
    single { AppDatabase.getInstance(androidContext()) }
}
