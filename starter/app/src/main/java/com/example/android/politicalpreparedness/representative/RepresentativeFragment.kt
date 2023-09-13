package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.util.LocationUtils
import com.example.android.politicalpreparedness.util.openApSetting
import com.example.android.politicalpreparedness.util.showAlterDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Locale

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentRepresentativeBinding, RepresentativeViewModel>() {
    override val viewModel: RepresentativeViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_PERMISSION_CODE = 111
        private const val TURN_USER_GPS_CODE = 112
        private const val FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val CROSS_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }


    override fun initViews() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        handelSpinnerItemSelected()
        binding.buttonLocation.setOnClickListener {
            hideKeyboard()
            checkLocationPermissions()
        }
        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            viewModel.getRepresentative()
        }
        setupRecRrepresentative()

    }

    private fun setupRecRrepresentative() {
        val representativeAdapter = RepresentativeListAdapter()
        binding.recRepresentatives.adapter = representativeAdapter
        viewModel.representatives.observe(viewLifecycleOwner) { representatives ->
            representativeAdapter.submitList(representatives)
        }
    }

    private fun handelSpinnerItemSelected() {
        binding.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.setStateSelected(binding.state.selectedItem.toString())
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                viewModel.setStateSelected(binding.state.selectedItem.toString())
            }
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRepresentativeBinding.inflate(inflater)


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TURN_USER_GPS_CODE -> {
                getUserLocation()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            TURN_USER_GPS_CODE -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        getUserLocation()
                    } else {
                        showSnackbar(
                            "Settings",
                            getString(R.string.location_permission_is_required_to_fetch_the_current_address)
                        ) {
                            requireActivity().openApSetting()
                        }

                    }
                }
            }
        }
    }


    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)?.map { address ->
            Address(
                address.thoroughfare ?: "",
                address.subThoroughfare,
                address.locality,
                address.adminArea ?: "",
                address.postalCode ?: ""
            )
        }?.first() ?: Address("", "", "", "", "")
    }


    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        val task = fusedLocationClient.lastLocation
        task.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val address = geoCodeLocation(location)
                Timber.e(address.toString())
                viewModel.setAddressOfUser(address)
                selectStateInSpinner(address)
            } else {
                openGpsSettings()
            }
        }
    }


    private fun selectStateInSpinner(address: Address) {
        val states = resources.getStringArray(R.array.states)
        val index = states.indexOfFirst { it == address.state }
        binding.state.setSelection(if (index != -1) index else 0)
    }

    private fun checkLocationPermissions() {
        if (LocationUtils.isPermissionGranted(requireContext())) {
            getUserLocation()
        } else {

            showSnackbar(getString(R.string.please_allow_location_permission), getString(R.string.allow)) {
                requestPermissions(
                    arrayOf(
                        FINE_LOCATION_PERMISSION,
                        CROSS_LOCATION_PERMISSION
                    ),
                    LOCATION_PERMISSION_CODE
                )
            }
        }
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun openGpsSettings() {
        showAlterDialog(
            getString(R.string.location_required),
            getString(R.string.please_enable_location_service)
        ) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(intent, TURN_USER_GPS_CODE)
        }

    }
}