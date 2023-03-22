package com.filipe1309.abasteceai.features.comparator.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.filipe1309.abasteceai.features.comparator.R
import com.filipe1309.abasteceai.features.comparator.databinding.FragmentComparatorBinding
import com.filipe1309.abasteceai.features.comparator.domain.entity.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ComparatorFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentComparatorBinding
    private val viewModel: ComparatorViewModel by viewModels {ComparatorViewModelFactory(requireContext())}
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentComparatorBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupSpinners()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (hasPermissions(requireContext(), PERMISSIONS)) {
            Log.d(TAG, "onCreateView: hasPermissions")
            getLastLocation()
        } else {
            Log.d(TAG, "onCreateView: requestPermissions")
            requestPermissionsLauncher.launch(PERMISSIONS)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupStateObservers()
        setupActionObservers()
    }

    private fun setupActionObservers() {
        Log.d(TAG, "setupActionObservers")
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.action.collect { action ->
                    when (action) {
                        is ComparatorAction.ShowSnackBar -> renderSnackBar(action.message)
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.firstFuelPrice.doAfterTextChanged {
            Log.d(TAG, "firstFuelPrice.doAfterTextChanged firstFuelValue: $it")
            Log.d(TAG, "firstFuelPrice.doAfterTextChanged secondFuelValue: ${binding.secondFuelPrice.text}")
            if (binding.secondFuelPrice.text.isNotEmpty())
                viewModel.dispatchViewIntent(ComparatorViewIntent.OnFuelTyped(
                    firstFuelPrice = it.toString().toDouble(),
                    secondFuelPrice = binding.secondFuelPrice.text.toString().toDouble()
                ))
        }

        binding.secondFuelPrice.doAfterTextChanged {
            Log.d(TAG, "secondFuelPrice.doAfterTextChanged secondFuelValue: $it")
            Log.d(TAG, "secondFuelPrice.doAfterTextChanged firstFuelValue: ${binding.firstFuelPrice.text}")
            if (binding.firstFuelPrice.text.isNotEmpty())
                viewModel.dispatchViewIntent(ComparatorViewIntent.OnFuelTyped(
                    firstFuelPrice = binding.firstFuelPrice.text.toString().toDouble(),
                    secondFuelPrice = it.toString().toDouble()
                ))
        }

        binding.btnAddFirstFuel.setOnClickListener {
            viewModel.dispatchViewIntent(ComparatorViewIntent.OnAddFuelClicked(it.id == R.id.btn_add_first_fuel, false))
        }

        binding.btnAddFirstFuel.setOnLongClickListener {
            viewModel.dispatchViewIntent(ComparatorViewIntent.OnAddFuelClicked(it.id == R.id.btn_add_first_fuel, true))
            true
        }

        binding.btnRemoveFirstFuel.setOnClickListener {
            viewModel.dispatchViewIntent(
                ComparatorViewIntent.OnSubtractFuelClicked(it.id == R.id.btn_remove_first_fuel, false)
            )
        }

        binding.btnRemoveFirstFuel.setOnLongClickListener {
            viewModel.dispatchViewIntent(
                ComparatorViewIntent.OnSubtractFuelClicked(it.id == R.id.btn_remove_first_fuel, true)
            )
            true
        }

        binding.btnAddSecondFuel.setOnClickListener {
            viewModel.dispatchViewIntent(ComparatorViewIntent.OnAddFuelClicked(it.id == R.id.btn_add_first_fuel, false))
        }

        binding.btnAddSecondFuel.setOnLongClickListener {
            viewModel.dispatchViewIntent(ComparatorViewIntent.OnAddFuelClicked(it.id == R.id.btn_add_first_fuel, true))
            true
        }

        binding.btnRemoveSecondFuel.setOnClickListener {
            viewModel.dispatchViewIntent(
                ComparatorViewIntent.OnSubtractFuelClicked(it.id == R.id.btn_remove_first_fuel, false)
            )
        }
        binding.btnRemoveSecondFuel.setOnLongClickListener {
            viewModel.dispatchViewIntent(
                ComparatorViewIntent.OnSubtractFuelClicked(it.id == R.id.btn_remove_first_fuel, true)
            )
            true
        }
        binding.fab.setOnClickListener { viewModel.dispatchViewIntent(ComparatorViewIntent.OnSaveClicked) }
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

    private fun setupStateObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState ->
                    render(uiState) }
            }
        }
    }

    private fun render(uiState: ComparatorViewState) {
        Log.d(TAG, "render: $uiState")
        with (uiState) {
            if (isFuelsReadyToCompare && !isLoading) {
                renderPrices(firstFuelPrice, secondFuelPrice)
                renderColor(firstFuelColor, secondFuelColor)
                if (isFuelsUpdated) renderSpinner(fuels!!.map { it.name })
            }
        }
    }

    private fun renderSnackBar(messageId: Int) {
        Log.d(TAG, "renderSnackBar")
        Snackbar.make(binding.root, messageId, Snackbar.LENGTH_SHORT).show()
        viewModel.dispatchViewIntent(ComparatorViewIntent.OnSnackBarRendered)
    }

    private fun renderSpinner(fuels: List<String>) {
        Log.d(TAG, "renderSpinner: arrayAdapter $arrayAdapter")
        // this.arrayAdapter.clear()
        this.arrayAdapter.addAll(fuels)
        Log.d(TAG, "- renderSpinner: fuels ${fuels}")
        if (fuels.size > 1) {
            binding.spinnerFirstFuel.setSelection(1, false)
            binding.spinnerSecondFuel.setSelection(2, false)
            viewModel.dispatchViewIntent(ComparatorViewIntent.OnSpinnerRendered)
        }
    }

    private fun renderColor(firstFuelColor: Int, secondFuelColor: Int) {
        Log.d(TAG, "renderColor: ${firstFuelColor} ${secondFuelColor}")
        binding.cardViewFirstFuel.setCardBackgroundColor(
            ContextCompat.getColor(requireActivity(), firstFuelColor)
        )
        binding.cardViewSecondFuel.setCardBackgroundColor(
            ContextCompat.getColor(requireActivity(), secondFuelColor)
        )
    }

    private fun renderPrices(firstFuelPrice: Double, secondFuelPrice: Double) {
        Log.d(TAG, "renderPrices: ${firstFuelPrice} ${secondFuelPrice}")
        if (hasPriceChanged(firstFuelPrice, binding.firstFuelPrice.text.toString()))
            binding.firstFuelPrice.setText(firstFuelPrice.toString())
        if (hasPriceChanged(secondFuelPrice, binding.secondFuelPrice.text.toString()))
            binding.secondFuelPrice.setText(secondFuelPrice.toString())
    }

    private fun hasPriceChanged(price: Double, priceText: String): Boolean {
        return priceText.isEmpty() || (priceText.isNotEmpty() && price != priceText.toDouble())
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
        adapterView?.tooltipText = arrayAdapter.getItem(i)
        if (adapterView?.id == R.id.spinner_first_fuel)
            viewModel.dispatchViewIntent(ComparatorViewIntent.OnFuelSelected(true, i-1))
        else
            viewModel.dispatchViewIntent(ComparatorViewIntent.OnFuelSelected(false, i-1))
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {
        adapterView?.tooltipText = "Select a fuel"
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                Log.d(TAG, "onCreateView: permissions granted")
                getLastLocation()
            }
        }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if (hasPermissions(requireContext(), PERMISSIONS)) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    Log.d(TAG, "Location: ${location.latitude}, ${location.longitude}")

                    viewModel.dispatchViewIntent(
                        ComparatorViewIntent.OnLocationReceived(Location(location.latitude, location.longitude))
                    )
                }
            }
        }
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        val TAG: String = ComparatorFragment::class.java.simpleName
        var PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    }
}
