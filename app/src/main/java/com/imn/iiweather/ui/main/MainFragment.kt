package com.imn.iiweather.ui.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.imn.iiweather.R
import com.imn.iiweather.databinding.FragmentMainBinding
import com.imn.iiweather.domain.model.location.LocationModel
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

    private fun populateLocationData(locationModel: LocationModel) = with(binding) {
        latitudeTextView.text = getString(R.string.latitude_, locationModel.latitude)
        longitudeTextView.text = getString(R.string.longitude_, locationModel.longitude)
    }


    private fun listenOnLocationUpdates() {
        if (checkSelfPermissionCompat(LOCATION_PERMISSIONS)) {
            binding.coordinatorLayout.showSnackbar("Has Permission",
                Snackbar.LENGTH_SHORT) // TODO make snackbar s better and refactor permission utils

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
        if (isGranted.values.all { true }) {
            listenOnLocationUpdates()
            binding.coordinatorLayout.showSnackbar("Location Granted", Snackbar.LENGTH_SHORT)
        } else {
            binding.coordinatorLayout.showSnackbar(
                R.string.location_permission_required,
                Snackbar.LENGTH_INDEFINITE,
                R.string.ok
            ) {
                requestLocationPermission()
                requireView().showSnackbar("Location Denied", Snackbar.LENGTH_SHORT)
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
                requestLocationPermission()
            }

        } else {
            requireView().showSnackbar("Location Permission not available", Snackbar.LENGTH_SHORT)

            requestPermissionLauncher.launch(LOCATION_PERMISSIONS)
        }
    }

    companion object {
        private val LOCATION_PERMISSIONS = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    }
}