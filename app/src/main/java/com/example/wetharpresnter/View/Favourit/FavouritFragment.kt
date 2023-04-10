package com.example.wetharpresnter.View.Favourit

import android.app.Dialog
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.SearchView
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Netwoek.NetworkListener
import com.example.wetharpresnter.R
import com.example.wetharpresnter.Repo.Repository
import com.example.wetharpresnter.ViewModel.FavouriteViewModel.FavouriteViewModel
import com.example.wetharpresnter.ViewModel.FavouriteViewModel.FavouriteViewModelFactory
import com.example.wetharpresnter.databinding.FragmentFavouritBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 * Use the [FavouritFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouritFragment : Fragment(), OnMapReadyCallback, ShowFavLocationData {
    lateinit var binding: FragmentFavouritBinding
    lateinit var dialog: Dialog
    lateinit var map: MapView
    lateinit var btnSaveLocation: Button
    var lat: Double? = null
    var lon: Double? = null
    lateinit var viewModelFactory: FavouriteViewModelFactory
    lateinit var viewModelProvider: FavouriteViewModel
    lateinit var snakbar: Snackbar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavouritBinding.inflate(layoutInflater, container, false)
        dialogInit(savedInstanceState)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelFactory = FavouriteViewModelFactory(requireContext(),Repository)
        viewModelProvider =
            ViewModelProvider(requireActivity(), viewModelFactory).get(FavouriteViewModel::class.java)
        viewModelProvider.getFavLocations()
        viewModelProvider.updateDatabaseWeatherState()
        var favAdapter = FavouritLocationAdapter(arrayListOf(), viewModelProvider, this)
        binding.rvFavouritLocations.apply {
            adapter = favAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)

        }

        viewModelProvider.accessFavList.observe(requireActivity()) { list ->
            favAdapter.setFavList(list as ArrayList<WeatherData>)
            binding.swiperefresh.isRefreshing = false

        }

        binding.addLocation.setOnClickListener {
            showMap()
            locationSearch()

        }
        binding.swiperefresh.setOnRefreshListener {
            viewModelProvider.getFavLocations()
        }

    }

    private fun showMap() {
        if (NetworkListener.getConnectivity(requireContext())) {
            dialog.show()
        } else {
            snakbar = Snackbar.make(
                binding.rvFavouritLocations,
                "No Network Connection",
                Snackbar.LENGTH_LONG
            )
            snakbar.show()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        var selectedLocation = LatLng(63.0, 63.0)
        var markerOption = MarkerOptions().position(selectedLocation).title("selected")
        var marker = googleMap.addMarker(markerOption)
        googleMap.setOnMapLongClickListener { lis ->

            var selectedLocation = LatLng(lis.latitude, lis.longitude)
            marker?.position = selectedLocation
            lat = lis.latitude
            lon = lis.longitude

            btnSaveLocation.setOnClickListener {
                if (NetworkListener.getConnectivity(requireContext())) {
                    viewModelProvider.addToFav(lat.toString(), lon.toString())
                }
                dialog.dismiss()


            }
        }
    }

    fun dialogInit(savedInstanceState: Bundle?) {
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.map_picker)

        map = dialog.findViewById(R.id.mv_fav_locations)
        val window: Window? = dialog.getWindow()
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.MATCH_PARENT
        )
        map.getMapAsync(this)
        map.onCreate(savedInstanceState)
        btnSaveLocation = dialog.findViewById(R.id.btn_save_location)

        btnSaveLocation.setOnClickListener {
            dialog.dismiss()

        }
    }

    override fun onResume() {
        super.onResume()
        viewModelProvider.getFavLocations()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
        binding.fragmentContainerView.visibility = View.GONE
        binding.rvFavouritLocations.visibility = View.VISIBLE
        binding.addLocation.visibility = View.VISIBLE
        binding.swiperefresh.isEnabled = true

    }

    override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        map.onStop()
    }

    override fun onStart() {
        super.onStart()
        map.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }

    fun locationSearch() {
        dialog.findViewById<SearchView>(R.id.sv_location_search).setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                var geoCoder = Geocoder(requireContext())
                var addressList = arrayListOf<Address>()
                addressList = geoCoder.getFromLocationName(query, 1) as ArrayList<Address>
                if (addressList.size > 0) {
                    var address = addressList.get(0)
                    var lat = address.latitude
                    var long = address.longitude
                    goToAddress(lat, long, 10f)

                }
                return false
            }

        })


    }

    private fun goToAddress(lat: Double, lon: Double, fl: Float) {
        var latLang = LatLng(lat, lon)
        var camera = CameraUpdateFactory.newLatLngZoom(latLang, fl)
        map.getMapAsync { it.animateCamera(camera) }
    }

    override fun show(weatherData: WeatherData) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, ShowFavouriteLocationsData(weatherData))
            .addToBackStack(null).commit()
        binding.fragmentContainerView.visibility = View.VISIBLE
        binding.rvFavouritLocations.visibility = View.GONE
        binding.addLocation.visibility = View.GONE
        binding.swiperefresh.isEnabled = false


    }
}
