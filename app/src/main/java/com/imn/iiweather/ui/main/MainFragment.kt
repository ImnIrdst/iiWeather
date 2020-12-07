package com.imn.iiweather.ui.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.imn.iiweather.IIWeatherApp
import com.imn.iiweather.R
import com.imn.iiweather.databinding.FragmentMainBinding
import com.imn.iiweather.domain.model.location.WeatherModel
import com.imn.iiweather.domain.utils.IIError
import com.imn.iiweather.domain.utils.Loading
import com.imn.iiweather.domain.utils.State
import com.imn.iiweather.domain.utils.humanReadable
import com.imn.iiweather.utils.checkSelfPermissionCompat
import com.imn.iiweather.utils.setTextOrGone
import com.imn.iiweather.utils.shouldShowRequestPermissionRationaleCompat
import com.imn.iiweather.utils.showSnackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var snackBar: Snackbar? = null

    private val locationViewModel by viewModels<MainViewModel> {
        (requireContext().applicationContext as IIWeatherApp).mainViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentMainBinding.inflate(inflater).also { _binding = it; initUI() }.root

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenOnLocationAndWeatherUpdates()
    }

    private fun handleState(state: State<WeatherModel>) = with(binding) {

        progressBar.isVisible = state.isLoading

        when (val value = state.value) {
            is WeatherModel -> {
                populateUI(value)
            }
            is IIError -> {
                populateUI(null)
                snackBar = coordinatorLayout.showSnackbar(value.humanReadable(),
                    Snackbar.LENGTH_INDEFINITE,
                    R.string.retry) {
                    listenOnLocationAndWeatherUpdates()
                }
            }
            is Loading -> populateUI(null)
            else -> {
                throw IllegalStateException()
            }
        }
    }

    private fun initUI() {
        populateUI(null)
    }

    private fun populateUI(weather: WeatherModel?) = with(binding) {
        snackBar?.dismiss()

        timeTextView.setOnClickListener {
            findNavController().navigate(R.id.leakTestFragment)
        }

        timeTextView.setTextOrGone(
            weather?.formattedDate?.let { getString(R.string.time_, it) }
        )
        summaryTextView.setTextOrGone(
            weather?.summary?.let { getString(R.string.summary_, it) }
        )
        temperatureTextView.setTextOrGone(
            weather?.temperature?.let { getString(R.string.temperature_, it) }
        )
        humidityTextView.setTextOrGone(
            weather?.humidity?.let { getString(R.string.humidity_, it) }
        )
        pressureTextView.setTextOrGone(
            weather?.pressure?.let { getString(R.string.pressure_, it) }
        )
        windSpeedTextView.setTextOrGone(
            weather?.windSpeed?.let { getString(R.string.windSpeed_, it) }
        )
        latitudeTextView.setTextOrGone(
            weather?.latitude?.let { getString(R.string.latitude_, it) }
        )
        longitudeTextView.setTextOrGone(
            weather?.longitude?.let { getString(R.string.longitude_, it) }
        )
    }

    private fun listenOnLocationAndWeatherUpdates() {
        if (checkSelfPermissionCompat(LOCATION_PERMISSIONS)) {
            locationViewModel.loadWeather().observe(viewLifecycleOwner) { state ->
                state?.let { handleState(it) }
            }
        } else {
            requestLocationPermission()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        if (isGranted.values.all { it == true }) {
            snackBar = binding.coordinatorLayout.showSnackbar(
                getString(R.string.location_permission_granted),
                Snackbar.LENGTH_SHORT
            )
            listenOnLocationAndWeatherUpdates()
        } else {
            snackBar = binding.coordinatorLayout.showSnackbar(
                R.string.location_permission_required,
                Snackbar.LENGTH_INDEFINITE,
                R.string.ok
            ) {
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        if (shouldShowRequestPermissionRationaleCompat(LOCATION_PERMISSIONS)) {
            snackBar = binding.coordinatorLayout.showSnackbar(
                R.string.location_permission_required,
                Snackbar.LENGTH_INDEFINITE,
                R.string.ok
            ) {
                requestPermissionLauncher.launch(LOCATION_PERMISSIONS)
            }

        } else {
            requestPermissionLauncher.launch(LOCATION_PERMISSIONS)
        }
    }

    companion object {
        private val LOCATION_PERMISSIONS = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    }
}