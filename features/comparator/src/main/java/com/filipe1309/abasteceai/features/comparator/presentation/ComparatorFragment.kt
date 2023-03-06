package com.filipe1309.abasteceai.features.comparator.presentation

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.filipe1309.abasteceai.features.comparator.R
import com.filipe1309.abasteceai.features.comparator.data.repository.FuelRepositoryImpl
import com.filipe1309.abasteceai.features.comparator.databinding.FragmentComparatorBinding
import com.filipe1309.abasteceai.features.comparator.domain.entity.Fuel
import com.filipe1309.abasteceai.features.comparator.domain.usecase.CompareFuelsUseCase
import com.filipe1309.abasteceai.features.comparator.domain.usecase.GetFuelsUseCase

private const val TAG = "ComparatorFragment"

class ComparatorFragment : Fragment(), AdapterView.OnItemSelectedListener  {
    private lateinit var binding: FragmentComparatorBinding
    private lateinit var viewModel: ComparatorViewModel
    private lateinit var factory: ComparatorViewModelFactory
    private var fuels = listOf<Fuel>()
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        binding = FragmentComparatorBinding.inflate(inflater, container, false)
        val fuelRepository = FuelRepositoryImpl()
        val compareFuelsUseCase = CompareFuelsUseCase(fuelRepository)
        val getFuelsUseCase = GetFuelsUseCase(fuelRepository)
        factory = ComparatorViewModelFactory(compareFuelsUseCase, getFuelsUseCase)
        viewModel = ViewModelProvider(this, factory)[ComparatorViewModel::class.java]

        setupSpinners()
        setupObservers()

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
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
        viewModel.getFuels().observe(viewLifecycleOwner) {
            fuels = it
            for (fuel in it) {
                arrayAdapter.add(fuel.name)
            }
            arrayAdapter.notifyDataSetChanged()
            if (arrayAdapter.count > 2) {
                binding.spinnerFirstFuel.setSelection(1, false)
                viewModel.firstFuel.value = it[0]

                binding.spinnerSecondFuel.setSelection(2, false)
                viewModel.secondFuel.value = it[1]
            }
            Log.i(TAG, "Fuels: $it")
        }

        viewModel.result.observe(viewLifecycleOwner) {
            if (it.fuel == viewModel.firstFuel.value) {
                binding.cardViewFirstFuel.setCardBackgroundColor(
                    ContextCompat.getColor(requireActivity(), android.R.color.holo_green_light)
                )
                binding.cardViewSecondFuel.setCardBackgroundColor(
                    ContextCompat.getColor(requireActivity(), android.R.color.holo_red_light)
                )
            } else {
                binding.cardViewFirstFuel.setCardBackgroundColor(
                    ContextCompat.getColor(requireActivity(), android.R.color.holo_red_light)
                )
                binding.cardViewSecondFuel.setCardBackgroundColor(
                    ContextCompat.getColor(requireActivity(), android.R.color.holo_green_light)
                )
            }
            Toast.makeText(
                context,
                "The best fuel is ${it.fuel.name}, with a cost of ${it.costPerUnitDistance} per unit distance",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
        val fuel = fuels[i - 1]
        Log.i(TAG, "Selected fuel: $fuel")
        if (adapterView == binding.spinnerFirstFuel) {
            viewModel.firstFuel.value = fuel
        } else {
            viewModel.secondFuel.value = fuel
        }
        viewModel.compareFuels()
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {
        adapterView?.tooltipText = "Select a fuel"
    }
}
