package com.example.wetharpresnter.View.Home

import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.wetharpresnter.*
import com.example.wetharpresnter.ViewModel.ViewModelFactory
import com.example.wetharpresnter.ViewModel.WeatherViewModel
import com.example.wetharpresnter.databinding.FragmentHomeBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeFragment(var viewPager: ViewPager2) : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentHomeBinding
    lateinit var configrations: SharedPreferences
    lateinit var btnSaveLocation: Button
    var lat: Double? = null
    var lon: Double? = null
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModelProvider: WeatherViewModel
    lateinit var dialog: Dialog
    lateinit var map: MapView


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        configrations = activity?.getSharedPreferences("Configuration", MODE_PRIVATE)!!
        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogInit(savedInstanceState)
        viewModelFactory = ViewModelFactory(requireContext())
        viewModelProvider = ViewModelProvider(requireActivity(), viewModelFactory).get(
            WeatherViewModel::class.java
        )
        if (configrations.getString(Constants.LOCATION, "").equals(Constants.GPS)) {
            getAndSetWeatherDataFromGPS()
        } else if (configrations.getString(Constants.LOCATION, "").equals(Constants.MAP)) {
            dialog.show()
        }

        binding.swiperefresh.setOnRefreshListener {
            if (configrations.getString(Constants.LOCATION, "").equals(Constants.GPS)) {
                getAndSetWeatherDataFromGPS()
            } else if (configrations.getString(Constants.LOCATION, "").equals(Constants.MAP)) {
                dialog.show()
            }
        }
        binding.shimmerViewContainer.startShimmer() // If auto-start is set to false


    }

    override fun onResume() {
        super.onResume()
        map.onResume()
        binding.swiperefresh.isRefreshing = true
        binding.shimmerViewContainer.startShimmer() // If auto-start is set to false
        if (configrations.getString(Constants.LOCATION, "").equals(Constants.GPS)) {
            getAndSetWeatherDataFromGPS()
        } else if (configrations.getString(Constants.LOCATION, "").equals(Constants.MAP)) {
            dialog.show()
        }


    }

    fun getAndSetWeatherDataFromGPS() {
        if (configrations.getString(Constants.LANG, "").equals(Constants.ARABIC)) {
            viewModelProvider.getLocation(Constants.ARABIC)
        } else {
            viewModelProvider.getLocation()
        }

        viewModelProvider.accessList.observe(requireActivity()) { weatherData ->

            binding.tvCityName.text = weatherData.timezone
            var temp = Math.ceil(weatherData.current?.temp ?: 0.0).toInt()

            binding.tvTempreture.text = temp.toString() + "°C"
            binding.tvWetharState.text = weatherData.current?.weather?.get(0)?.main

            var uri =
                "https://openweathermap.org/img/wn/${weatherData.current?.weather?.get(0)?.icon}@2x.png"
            Glide.with(requireActivity()).load(uri).into(binding.ivWetharState)
            binding.tvHumidity.text =
                Math.ceil((weatherData.current?.humidity)?.toDouble() ?: 0.0).toInt().toString()
            binding.tvPressure.text =
                Math.ceil((weatherData.current?.pressure)?.toDouble() ?: 0.0).toInt().toString()
            binding.tvWindSpeed.text =
                Math.ceil(weatherData.current?.wind_speed ?: 5.0).toInt().toString()

            binding.rvHoursWeather.apply {
                adapter = HoursWeatherDataAdapter(weatherData.hourly)
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            }

            binding.rvDayWeather.apply {
                adapter = DaysWeatherDataAdapter(weatherData.daily)
                layoutManager = LinearLayoutManager(requireContext())
            }

            binding.shimmerViewContainer.hideShimmer()
            binding.swiperefresh.isRefreshing = false
        }

        handelViewPagerWithRecycleView()
    }

    fun getAndSetWeatherDataFromMap(lat: String, lon: String) {
        if (configrations.getString(Constants.LANG, "").equals(Constants.ARABIC)) {
            viewModelProvider.getWeatherDataFromApi(lat, lon, Constants.ARABIC)
        } else {
            viewModelProvider.getWeatherDataFromApi(lat, lon)
        }

        viewModelProvider.accessList.observe(requireActivity()) { weatherData ->

            binding.tvCityName.text = weatherData.timezone
            var temp = Math.ceil(weatherData.current?.temp ?: 0.0).toInt()

            binding.tvTempreture.text = temp.toString() + "°C"
            binding.tvWetharState.text = weatherData.current?.weather?.get(0)?.main

            var uri =
                "https://openweathermap.org/img/wn/${weatherData.current?.weather?.get(0)?.icon}@2x.png"
            Glide.with(requireActivity()).load(uri).into(binding.ivWetharState)
            binding.tvHumidity.text =
                Math.ceil((weatherData.current?.humidity)?.toDouble() ?: 0.0).toInt().toString()
            binding.tvPressure.text =
                Math.ceil((weatherData.current?.pressure)?.toDouble() ?: 0.0).toInt().toString()
            binding.tvWindSpeed.text =
                Math.ceil(weatherData.current?.wind_speed ?: 5.0).toInt().toString()

            binding.rvHoursWeather.apply {
                adapter = HoursWeatherDataAdapter(weatherData.hourly)
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            }

            binding.rvDayWeather.apply {
                adapter = DaysWeatherDataAdapter(weatherData.daily)
                layoutManager = LinearLayoutManager(requireContext())
            }

            binding.shimmerViewContainer.hideShimmer()
            binding.swiperefresh.isRefreshing = false
        }

        handelViewPagerWithRecycleView()
    }

    fun handelViewPagerWithRecycleView() {
        binding.rvHoursWeather.addOnItemTouchListener(object : OnItemTouchListener {
            var lastX = 0
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> lastX = e.x.toInt()
                    MotionEvent.ACTION_MOVE -> {
                        val isScrollingRight = e.x < lastX
                        if (isScrollingRight && (binding.rvHoursWeather.getLayoutManager() as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == (binding.rvHoursWeather.getAdapter()
                                ?.getItemCount()
                                ?: 0) - 1 || !isScrollingRight && (binding.rvHoursWeather.getLayoutManager() as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0
                        ) {
                            viewPager.setUserInputEnabled(true)
                        } else {
                            viewPager.setUserInputEnabled(false)
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        lastX = 0
                        viewPager.setUserInputEnabled(true)
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    override fun onPause() {
        super.onPause()
        Log.i(
            "TAG",
            "onRadioButtonClicked: " + configrations.getString(Constants.LANG, "not found")
        )
        map.onPause()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        var selectedLocation = LatLng(63.0, 63.0)
        var markerOption = MarkerOptions().position(selectedLocation).title("selected")
        var marker = googleMap.addMarker(markerOption)
        googleMap.setOnMapLongClickListener { lis ->

            var selectedLocation = LatLng(lis.latitude, lis.longitude)
            marker?.position = selectedLocation
            btnSaveLocation.setOnClickListener {
                lat = lis.latitude
                lon = lis.longitude
                getAndSetWeatherDataFromMap(lat.toString(), lon.toString())

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


}