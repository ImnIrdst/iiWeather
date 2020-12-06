package com.imn.iiweather.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.imn.iiweather.R
import com.imn.iiweather.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val locationViewModel by viewModels<LocationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentMainBinding.inflate(inflater).also { binding = it; initUI() }.root

    private fun initUI() = with(binding) {
        latitudeTextView.text = getString(R.string.latitude_, 37.3806)
        longitudeTextView.text = getString(R.string.longitude_, -122.0593)
    }
}