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
import com.imn.iiweather.R
import com.imn.iiweather.databinding.FragmentMainBinding
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.utils.State
import com.imn.iiweather.domain.utils.humanReadable
import com.imn.iiweather.ui.common.location.LocationViewModel
import com.imn.iiweather.utils.checkSelfPermissionCompat
import com.imn.iiweather.utils.shouldShowRequestPermissionRationaleCompat
import com.imn.iiweather.utils.showSnackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val locationViewModel by viewModels<LocationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentMainBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenOnLocationUpdates()
    }

    private fun populateLocationData(state: State<LocationModel>?) = with(binding) {

        progressBar.isVisible = state is State.Loading

        when (state) {
            is State.Success -> {
                longitudeTextView.text = getString(R.string.longitude_, state.value.longitude)
                latitudeTextView.text = getString(R.string.latitude_, state.value.latitude)
            }
            is State.Failure -> {
                coordinatorLayout.showSnackbar(state.error.humanReadable(),
                    Snackbar.LENGTH_INDEFINITE,
                    R.string.retry) {
                    listenOnLocationUpdates()
                }
            }
            else -> Unit
        }
    }


    private fun listenOnLocationUpdates() {
        if (checkSelfPermissionCompat(LOCATION_PERMISSIONS)) {
            locationViewModel.getLocationData().observe(viewLifecycleOwner) {
                populateLocationData(it)
            }
        } else {
            requestLocationPermission()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        if (isGranted.values.all { it == true }) {
            listenOnLocationUpdates()
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