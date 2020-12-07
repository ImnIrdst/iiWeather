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
import com.imn.iiweather.utils.shouldShowRequestPermissionRationaleCompat
import com.imn.iiweather.utils.showSnackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val locationViewModel by viewModels<MainViewModel> {
        (requireContext().applicationContext as IIWeatherApp).mainViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentMainBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenOnLocationAndWeatherUpdates()
    }

    private fun populateUI(state: State<WeatherModel>) = with(binding) {

        progressBar.isVisible = state.isLoading

        when (state.value) {
            is WeatherModel -> {
                longitudeTextView.text = getString(R.string.longitude_, state.value.longitude)
                latitudeTextView.text = getString(R.string.latitude_, state.value.latitude)
            }
            is IIError -> {
                coordinatorLayout.showSnackbar(state.value.humanReadable(),
                    Snackbar.LENGTH_INDEFINITE,
                    R.string.retry) {
                    listenOnLocationAndWeatherUpdates()
                }
            }
            is Loading -> Unit
            else -> {
                throw IllegalStateException()
            }
        }
    }


    private fun listenOnLocationAndWeatherUpdates() {
        if (checkSelfPermissionCompat(LOCATION_PERMISSIONS)) {
            locationViewModel.loadWeather().observe(viewLifecycleOwner) { state ->
                state?.let { populateUI(it) }
            }
        } else {
            requestLocationPermission()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        if (isGranted.values.all { it == true }) {
            listenOnLocationAndWeatherUpdates()
            binding.coordinatorLayout.showSnackbar(
                getString(R.string.location_permission_granted),
                Snackbar.LENGTH_SHORT
            )
        } else {
            binding.coordinatorLayout.showSnackbar(
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
            binding.coordinatorLayout.showSnackbar(
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