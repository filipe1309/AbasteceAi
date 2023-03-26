package com.filipe1309.abasteceai.ui.comparator

import androidx.annotation.VisibleForTesting
import com.filipe1309.abasteceai.domain.comparator.usecase.CompareFuelsUseCase
import com.filipe1309.abasteceai.domain.fuels.usecase.GetFuelsUseCase
import com.filipe1309.abasteceai.domain.fuels.usecase.UpdateFuelsUseCase
import com.filipe1309.abasteceai.domain.histories.usecase.SaveComparisonUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object ComparatorModules {
    private val module = module {
        viewModel { ComparatorViewModel(get()) }

        // Use cases
        factory { CompareFuelsUseCase() }
        factory { GetFuelsUseCase(get()) }
        factory { SaveComparisonUseCase(get()) }
        factory { UpdateFuelsUseCase(get()) }
        factory { UseCasesComparator(get(), get(), get(), get()) }
    }

    fun load() = loadKoinModules(listOf(module))

    @VisibleForTesting
    fun getModules(): List<Module> {
        return listOf(module)
    }
}
