package com.example.wetharpresnter.View.Home

import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.RemoteException
import android.provider.Settings
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.wetharpresnter.*
import com.example.wetharpresnter.Location.GPSLocation
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Netwoek.ApiState
import com.example.wetharpresnter.Netwoek.NetworkListener
import com.example.wetharpresnter.Utils.Constants
import com.example.wetharpresnter.ViewModel.HomeViewModel.ViewModelFactory
import com.example.wetharpresnter.ViewModel.HomeViewModel.HomeViewModel
import com.example.wetharpresnter.databinding.FragmentHomeBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


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
    lateinit var viewModelProvider: HomeViewModel
    lateinit var dialog: Dialog
    lateinit var map: MapView
    lateinit var geoCoder: Geocoder
    var addressList = arrayListOf<Address>()
    lateinit var snakbar: Snackbar
    lateinit var gpsLocation: GPSLocation


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
        gpsLocation = GPSLocation(requireContext())

        geoCoder = Geocoder(requireContext())
        dialogInit(savedInstanceState)
        viewModelFactory = ViewModelFactory(requireContext())
        viewModelProvider = ViewModelProvider(requireActivity(), viewModelFactory).get(
            HomeViewModel::class.java
        )
        binding.swiperefresh.isRefreshing = false
        if (NetworkListener.getConnectivity(requireContext())) {
            binding.swiperefresh.isRefreshing = true
            if (configrations.getString(Constants.LOCATION, "").equals(Constants.GPS)) {
                getAndSetWeatherDataFromGPS()
            } else if (configrations.getString(Constants.LOCATION, "").equals(Constants.MAP)) {
                dialog.show()
                Constants.mapFlag = false
            }

            binding.shimmerViewContainer.startShimmer() // If auto-start is set to false
        } else {
            snakbar = Snackbar.make(
                view.findViewById(R.id.scrol_view),
                "No Network Connection",
                Snackbar.LENGTH_LONG
            )
            showTempWeatherData()
            snakbar.show()
        }
        onRefresh()


    }


    fun showTempWeatherData() {
        if (configrations.contains("tempWethearData")) {
            binding.shimmerViewContainer.hideShimmer()
            binding.swiperefresh.isRefreshing = false
            var gson = Gson()
            var weatherData: WeatherData = gson.fromJson(
                configrations.getString("tempWethearData", ""),
                WeatherData::class.java
            )
            try {
                addressList =
                    geoCoder.getFromLocation(
                        weatherData.lat,
                        weatherData.lon,
                        1
                    ) as ArrayList<Address>
                if (addressList.size > 0) {
                    var address = addressList.get(0)

                    binding.tvCityName.text = address.countryName
                }

            } catch (e: IOException) {
                binding.tvCityName.text = weatherData.timezone
            } catch (e: RemoteException) {
                binding.tvCityName.text = weatherData.timezone

            }

            var temp = Math.ceil(weatherData.current?.temp ?: 0.0).toInt()

            binding.tvTempreture.text = temp.toString() + "°C"
            binding.tvWetharState.text = weatherData.current?.weather?.get(0)?.main

            binding.tvHumidity.text =
                Math.ceil((weatherData.current?.humidity)?.toDouble() ?: 0.0).toInt().toString()
            binding.tvPressure.text =
                Math.ceil((weatherData.current?.pressure)?.toDouble() ?: 0.0).toInt().toString()
            binding.tvWindSpeed.text =
                Math.ceil(weatherData.current?.wind_speed ?: 5.0).toInt().toString()

            binding.rvHoursWeather.apply {
                adapter = HoursWeatherDataAdapter(weatherData.hourly, configrations)
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            }

            binding.rvDayWeather.apply {
                adapter = DaysWeatherDataAdapter(weatherData.daily, configrations)
                layoutManager = LinearLayoutManager(requireContext())
            }


        }
        handelViewPagerWithRecycleView()
    }


    override fun onResume() {
        super.onResume()
        map.onResume()

            binding.swiperefresh.isRefreshing = true
            binding.shimmerViewContainer.showShimmer(true)
            binding.shimmerViewContainer.startLayoutAnimation()// If auto-start is set to false
            if (configrations.getString(Constants.LOCATION, "").equals(Constants.GPS)) {
                if (gpsLocation.checkPermission()) {
                    binding.shimmerViewContainer.visibility=View.VISIBLE

                    binding.cvPersmission.visibility=View.GONE


                getAndSetWeatherDataFromGPS()}

            } else if (configrations.getString(Constants.LOCATION, "").equals(Constants.MAP)) {
                if (Constants.mapFlag == true) {
                    dialog.show()
                    Constants.mapFlag = false

                } else {

                    if (addressList.size > 0) {
                        addressList.get(0).latitude
                        viewModelProvider.getWeatherDataFromApi(
                            addressList.get(0).latitude.toString(),
                            addressList.get(0).longitude.toString(),
                            configrations.getString(Constants.LANG, "eng")!!,
                            configrations.getString(Constants.UNITS, Constants.DEFAULT)!!
                        )
                    }
                }


        }
    }

    fun getAndSetWeatherDataFromGPS() {


            var lang = configrations.getString(Constants.LANG, "")
            var unit = configrations.getString(Constants.UNITS, "")
            viewModelProvider.getLocation(
                lang = lang ?: Constants.ENGLISH,
                unit ?: Constants.DEFAULT
            )
        if (gpsLocation.checkPermission()) {
            lifecycleScope.launch {
                viewModelProvider.accessList.collect() { result ->
                    when (result) {
                        is ApiState.Success -> {
                            var weatherData = result.date

                            var simpleDate = SimpleDateFormat("dd-M-yyyy")
                            var currentDate =
                                simpleDate.format(weatherData?.current?.dt?.times(1000L))
                            var date: Date = simpleDate.parse(currentDate)
                            println(date.toString())
                            var time = date.toString().split(" ")

                            binding.date.text = "${time[0]} ${time[2]} ${time[1]} ${time[5]}"

                            val gson = Gson()
                            val json: String = gson.toJson(weatherData)
                            configrations.edit().putString("tempWethearData", json).commit()

                            if (weatherData != null) {
                                addressList =
                                    geoCoder.getFromLocation(
                                        weatherData.lat,
                                        weatherData.lon,
                                        1
                                    ) as ArrayList<Address>
                            }
                            if (addressList.size > 0) {
                                var address = addressList.get(0)

                                binding.tvCityName.text = address.countryName
                            }

                            var temp = Math.ceil(weatherData?.current?.temp ?: 0.0).toInt()

                            var format = tempFormat(
                                temp.toString(),
                                Math.ceil(weatherData?.current?.wind_speed ?: 5.0).toInt()
                                    .toString()
                            )
                            binding.tvTempreture.text = format.first
                            binding.tvWetharState.text = weatherData?.current?.weather?.get(0)?.main

                            var uri =
                                "https://openweathermap.org/img/wn/${
                                    weatherData?.current?.weather?.get(
                                        0
                                    )?.icon
                                }@2x.png"
                            Glide.with(requireActivity()).load(uri).into(binding.ivWetharState)
                            binding.tvHumidity.text =
                                Math.ceil((weatherData?.current?.humidity)?.toDouble() ?: 0.0)
                                    .toInt()
                                    .toString()+"\n"+activity?.getString(R.string.humdity_unite)
                            binding.tvPressure.text =
                                Math.ceil((weatherData?.current?.pressure)?.toDouble() ?: 0.0)
                                    .toInt()
                                    .toString()+"\n"+activity?.getString(R.string.pressure_unite)
                            binding.tvWindSpeed.text = format.second

                            binding.rvHoursWeather.apply {
                                if (weatherData != null) {
                                    adapter =
                                        HoursWeatherDataAdapter(weatherData.hourly, configrations)
                                }
                                layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )

                            }

                            binding.rvDayWeather.apply {
                                if (weatherData != null) {
                                    adapter =
                                        DaysWeatherDataAdapter(weatherData.daily, configrations)
                                }
                                layoutManager = LinearLayoutManager(requireContext())
                            }
                            //   viewModelProvider.updateDatabaseWeatherState()

                            binding.shimmerViewContainer.hideShimmer()
                            binding.swiperefresh.isRefreshing = false
                        }
                        is ApiState.Failure -> {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
                        }
                        is ApiState.Loading -> {
                            binding.shimmerViewContainer.showShimmer(true)
                            binding.shimmerViewContainer.startShimmer()
                        }

                        else -> {}
                    }

                }

            }
        } else {
            binding.shimmerViewContainer.visibility = View.GONE
            binding.cvPersmission.visibility = View.VISIBLE
            binding.btnCheckPermission.setOnClickListener {

                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                requireActivity().startActivity(intent)

            }

        }





        handelViewPagerWithRecycleView()
    }

    fun getAndSetWeatherDataFromMap(lat: String, lon: String) {
        if (NetworkListener.getConnectivity(requireContext())){
        var lang = configrations.getString(Constants.LANG, "")
        var unit = configrations.getString(Constants.UNITS, "")

        viewModelProvider.getWeatherDataFromApi(
            lat,
            lon,
            lang ?: Constants.ENGLISH,
            unit ?: Constants.DEFAULT
        )

        lifecycleScope.launch {
            viewModelProvider.accessList.collect() { result ->
                when (result) {
                    is ApiState.Success -> {
                        var weatherData = result.date
                        val gson = Gson()
                        val json: String = gson.toJson(weatherData)
                        configrations.edit().putString("tempWethearData", json).commit()
                        if (weatherData != null) {
                            addressList =
                                geoCoder.getFromLocation(
                                    weatherData.lat,
                                    weatherData.lon,
                                    1
                                ) as ArrayList<Address>
                        }
                        if (addressList.size > 0) {
                            var address = addressList.get(0)

                            binding.tvCityName.text = address.countryName
                        }
                        var temp = Math.ceil(weatherData?.current?.temp ?: 0.0).toInt()

                        var format = tempFormat(
                            temp.toString(),
                            Math.ceil(weatherData?.current?.wind_speed ?: 5.0).toInt().toString()
                        )
                        binding.tvTempreture.text = format.first
                        binding.tvWetharState.text = weatherData?.current?.weather?.get(0)?.main


                        var uri =
                            "https://openweathermap.org/img/wn/${
                                weatherData?.current?.weather?.get(
                                    0
                                )?.icon
                            }@2x.png"
                        Glide.with(requireActivity()).load(uri).into(binding.ivWetharState)
                        binding.tvHumidity.text =
                            Math.ceil((weatherData?.current?.humidity)?.toDouble() ?: 0.0).toInt()
                                .toString()+"\n"+activity?.getString(R.string.humdity_unite)
                        binding.tvPressure.text =
                            Math.ceil((weatherData?.current?.pressure)?.toDouble() ?: 0.0).toInt()
                                .toString()+"\n"+activity?.getString(R.string.pressure_unite)
                        binding.tvWindSpeed.text = format.second

                        binding.rvHoursWeather.apply {
                            if (weatherData != null) {
                                adapter = HoursWeatherDataAdapter(weatherData.hourly, configrations)
                            }
                            layoutManager =
                                LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )

                        }

                        binding.rvDayWeather.apply {
                            if (weatherData != null) {
                                adapter = DaysWeatherDataAdapter(weatherData.daily, configrations)
                            }
                            layoutManager = LinearLayoutManager(requireContext())
                        }
                        //  viewModelProvider.updateDatabaseWeatherState()


                        binding.shimmerViewContainer.hideShimmer()
                        binding.swiperefresh.isRefreshing = false
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
                    }
                    is ApiState.Loading -> {
                        binding.shimmerViewContainer.showShimmer(true)
                        binding.shimmerViewContainer.startShimmer()
                    }
                    else -> {}
                }

            }

        }
        }
        else{
            Toast.makeText(requireContext(),"No network",Toast.LENGTH_LONG).show()
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

    fun tempFormat(temp: String, windSpeed: String): Pair<String, String> {
        var format = Pair("", "")
        if (configrations.getString(Constants.UNITS, "").equals(Constants.DEFAULT)) {
            format = Pair(temp + "°K", windSpeed + "\n" + getString(R.string.miles_hour))

        } else if (configrations.getString(Constants.UNITS, "").equals(Constants.METRIC)) {
            format = Pair(temp + "°C", windSpeed + "\n" + getString(R.string.meter_sec))

        } else if (configrations.getString(Constants.UNITS, "").equals(Constants.IMPERIAL)) {
            format = Pair(temp + "°F", windSpeed + "\n" + getString(R.string.miles_hour))
        }
        return format
    }

    fun onRefresh() {
        binding.swiperefresh.setOnRefreshListener {
            if (NetworkListener.getConnectivity(requireContext())) {
                if (configrations.getString(Constants.LOCATION, "").equals(Constants.GPS)) {
                    getAndSetWeatherDataFromGPS()
                } else if (configrations.getString(Constants.LOCATION, "")
                        .equals(Constants.MAP)
                ) {
                    if (addressList.size > 0) {
                        var address = addressList.get(0)
                        viewModelProvider.getWeatherDataFromApi(
                            address.latitude.toString(),
                            address.longitude.toString(),
                            configrations.getString(Constants.LANG, "en")!!,
                            configrations.getString(Constants.UNITS, Constants.DEFAULT)!!
                        )
                    }

                }
            } else {
                binding.swiperefresh.isRefreshing = false
                snakbar = Snackbar.make(
                    binding.scrolView,
                    "No Network Connection",
                    Snackbar.LENGTH_LONG
                )
                showTempWeatherData()
                snakbar.show()
            }
        }
    }


}