package com.example.wetharpresnter.View.Favourit

import android.content.Context
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.wetharpresnter.Utils.Constants
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Netwoek.ApiState
import com.example.wetharpresnter.Netwoek.NetworkListener
import com.example.wetharpresnter.R
import com.example.wetharpresnter.Repo.Repository
import com.example.wetharpresnter.View.Home.DaysWeatherDataAdapter
import com.example.wetharpresnter.View.Home.HoursWeatherDataAdapter
import com.example.wetharpresnter.ViewModel.HomeViewModel.HomeViewModel
import com.example.wetharpresnter.databinding.FragmentShowFavouritLocationsDataBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class ShowFavouriteLocationsData(var weatherData: WeatherData) : Fragment() {
    lateinit var binding: FragmentShowFavouritLocationsDataBinding

    lateinit var viewModelProvider: HomeViewModel
    lateinit var configrations: SharedPreferences
    lateinit var geoCoder: Geocoder
    var addressList = arrayListOf<Address>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowFavouritLocationsDataBinding.inflate(inflater, container, false)

        geoCoder = Geocoder(requireContext())
        viewModelProvider = HomeViewModel(requireContext(), Repository)

        configrations = activity?.getSharedPreferences("Configuration", Context.MODE_PRIVATE)!!


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (NetworkListener.getConnectivity(requireContext())){
            getdata()
            binding.swiperefresh.isEnabled = true
            binding.swiperefresh.setOnRefreshListener {
                getdata()
            }
        }else{
            binding.swiperefresh.isEnabled = false
            getWeatherData(weatherData)
        }

    }
    fun getdata(){

        viewModelProvider.getWeatherDataFromApi(
            weatherData.lat.toString(),
            weatherData.lon.toString(),
            configrations.getString(Constants.LANG, "")!!,
            configrations.getString(Constants.UNITS, "")!!
        )
        lifecycleScope.launch {
            viewModelProvider.accessList.collect() { result ->
                when (result) {
                    is ApiState.Success -> {
                        binding.swiperefresh.isRefreshing = false
                        binding.shimmerViewContainer.showShimmer(false)
                        binding.shimmerViewContainer.stopShimmer()
                        getWeatherData(result.date!!)
                    }
                    is ApiState.Failure -> {
                        var snakbar = Snackbar.make(
                           binding.swiperefresh,
                            "No Network Connection",
                            Snackbar.LENGTH_LONG

                        )
                        binding.swiperefresh.isRefreshing = false

                        snakbar.show()
                    }
                    is ApiState.Loading -> {
                        binding.swiperefresh.isRefreshing = true
                        binding.shimmerViewContainer.showShimmer(true)
                        binding.shimmerViewContainer.startShimmer()

                    }
                    else -> {}
                }
            }


        }
    }


    fun getWeatherData(weatherData: WeatherData) {
        /* if (NetworkListener.getConnectivity(requireContext())) {
             if (configrations.getString(Constants.LANG, "").equals(Constants.ARABIC)) {
                 viewModelProvider.addToFav(
                     weatherData.lat.toString(),
                     weatherData.lon.toString(),
                     Constants.ARABIC
                 )
             } else {
                 viewModelProvider.addToFav(
                     weatherData.lat.toString(),
                     weatherData.lon.toString()
                 )
             }

             viewModelProvider.accessList.observe(requireActivity()) { weatherData ->
                 _setWeatherData(weatherData)
             }


         } else {
             _setWeatherData(weatherData)
         }*/
        _setWeatherData(weatherData)
        /*    binding.swiperefresh.setOnRefreshListener {
                viewModelProvider.addToFav(
                    weatherData.lat.toString(),
                    weatherData.lon.toString()
                )
                viewModelProvider.accessList.observe(requireActivity()) { weatherData ->
                    _setWeatherData(weatherData)
                }
            }*/
    }

    fun _setWeatherData(weatherData: WeatherData) {
        var simpleDate = SimpleDateFormat("dd-M-yyyy")
        var currentDate = simpleDate.format(weatherData.current?.dt?.times(1000L))
        var date: Date = simpleDate.parse(currentDate)
        println(date.toString())
        var time = date.toString().split(" ")
        binding.date.text = "${time[0]} ${time[2]} ${time[1]} ${time[5]}"
        binding.swiperefresh.isRefreshing = false
        addressList =
            geoCoder.getFromLocation(weatherData.lat, weatherData.lon, 1) as ArrayList<Address>
        if (addressList.size > 0) {
            var address = addressList.get(0)

            binding.tvCityName.text = address.countryName
        }
        var temp = Math.ceil(weatherData.current?.temp ?: 0.0).toInt()

        var format = tempFormat(
            temp.toString(),
            Math.ceil(weatherData.current?.wind_speed ?: 5.0).toInt().toString()
        )
        binding.tvTempreture.text = format.first
        binding.tvWetharState.text = weatherData.current?.weather?.get(0)?.main

        var uri =
            "https://openweathermap.org/img/wn/${weatherData.current?.weather?.get(0)?.icon}@2x.png"
        Glide.with(requireActivity()).load(uri).into(binding.ivWetharState)
        binding.tvHumidity.text =
            Math.ceil((weatherData.current?.humidity)?.toDouble() ?: 0.0).toInt().toString()
        binding.tvPressure.text =
            Math.ceil((weatherData.current?.pressure)?.toDouble() ?: 0.0).toInt().toString()
        binding.tvWindSpeed.text = format.second

        binding.rvHoursWeather.apply {
            adapter = HoursWeatherDataAdapter(weatherData.hourly, configrations)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        }

        binding.rvDayWeather.apply {
            adapter = DaysWeatherDataAdapter(weatherData.daily, configrations)
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.shimmerViewContainer.hideShimmer()
        binding.swiperefresh.isRefreshing = false


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


}


