package com.filipe1309.abasteceai.features.comparator.presentation

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.filipe1309.abasteceai.features.comparator.R
import com.filipe1309.abasteceai.features.comparator.data.repository.FuelRepositoryImpl
import com.filipe1309.abasteceai.features.comparator.databinding.FragmentComparatorBinding
import com.filipe1309.abasteceai.features.comparator.domain.usecase.CompareFuelsUseCase
import com.filipe1309.abasteceai.features.comparator.domain.usecase.GetFuelsUseCase

private const val TAG = "ComparatorFragment"

class ComparatorFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentComparatorBinding
    private lateinit var viewModel: ComparatorViewModel
    private lateinit var factory: ComparatorViewModelFactory
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentComparatorBinding.inflate(inflater, container, false)
        val fuelRepository = FuelRepositoryImpl()
        val compareFuelsUseCase = CompareFuelsUseCase(fuelRepository)
        val getFuelsUseCase = GetFuelsUseCase(fuelRepository)
        val useCasesComparator = UseCasesComparator(compareFuelsUseCase, getFuelsUseCase)
        factory = ComparatorViewModelFactory(useCasesComparator)
        viewModel = ViewModelProvider(this, factory)[ComparatorViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupSpinners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActions()
        setupObservers()
    }

    private fun setupActions() {
        binding.firstFuelValue.doAfterTextChanged { viewModel.sendAction(ComparatorAction.FuelPriceUpdated) }
        binding.secondFuelValue.doAfterTextChanged { viewModel.sendAction(ComparatorAction.FuelPriceUpdated) }
        binding.btnAddFirstFuel.setOnClickListener {
            viewModel.sendAction(ComparatorAction.ButtonAddFuelClicked( it.id == R.id.btn_add_first_fuel))
        }
        binding.btnRemoveFirstFuel.setOnClickListener {
            viewModel.sendAction(ComparatorAction.ButtonRemoveFuelClicked( it.id == R.id.btn_remove_first_fuel))
        }
        binding.btnAddSecondFuel.setOnClickListener {
            viewModel.sendAction(ComparatorAction.ButtonAddFuelClicked( it.id == R.id.btn_add_first_fuel))
        }
        binding.btnRemoveSecondFuel.setOnClickListener {
            viewModel.sendAction(ComparatorAction.ButtonRemoveFuelClicked( it.id == R.id.btn_remove_first_fuel))
        }
    }

    private fun setupSpinners() {
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item_text, arrayListOf(""))
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(binding.spinnerFirstFuel) {
            adapter = arrayAdapter
            setSelection(0, false)
            onItemSelectedListener = this@ComparatorFragment
            prompt = getString(R.string.prompt_fuel)
            gravity = Gravity.CENTER
        }

        with(binding.spinnerSecondFuel) {
            adapter = arrayAdapter
            setSelection(0, false)
            onItemSelectedListener = this@ComparatorFragment
            prompt = getString(R.string.prompt_fuel)
            gravity = Gravity.CENTER
        }
    }

    private fun setupObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            render(viewState ?: return@Observer)
        })
    }

    private fun render(viewState: ComparatorViewState) {
        Log.d(TAG, "render: $viewState")
        if (viewState.isComparing && viewState.comparisonResult != null) {
            renderColor(viewState)
        }

        if (viewState.isFuelsLoaded && viewState.fuels != null && !viewState.isFuelsReadyToCompare) {
            renderSpinner(viewState)
        }
    }

    private fun renderSpinner(viewState: ComparatorViewState) {
        Log.d(TAG, "renderSpinner: arrayAdapter $arrayAdapter")
        // this.arrayAdapter.clear()
        this.arrayAdapter.addAll(viewState.fuels!!.map { it.name })
        Log.d(TAG, "renderSpinner: fuels ${viewState.fuels}")
        if (viewState.fuels.size > 1) {
            binding.spinnerFirstFuel.setSelection(1, false)
            binding.spinnerSecondFuel.setSelection(2, false)
            viewModel.sendAction(ComparatorAction.SpinnerRendered)
        }
        this.arrayAdapter.notifyDataSetChanged()

    }

    private fun renderColor(viewState: ComparatorViewState) {
        binding.cardViewFirstFuel.setCardBackgroundColor(
            ContextCompat.getColor(requireActivity(), viewState.firstFuel?.color!!)
        )
        binding.cardViewSecondFuel.setCardBackgroundColor(
            ContextCompat.getColor(requireActivity(), viewState.secondFuel?.color!!)
        )
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
        adapterView?.tooltipText = arrayAdapter.getItem(i)
        if (adapterView?.id == R.id.spinner_first_fuel)
            viewModel.sendAction(ComparatorAction.FuelSelected(i - 1, true))
        else
            viewModel.sendAction(ComparatorAction.FuelSelected(i - 1, false))
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {
        adapterView?.tooltipText = "Select a fuel"
    }
}
