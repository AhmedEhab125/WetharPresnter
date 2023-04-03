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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.wetharpresnter.Constants
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.NetworkListener
import com.example.wetharpresnter.R
import com.example.wetharpresnter.View.Home.DaysWeatherDataAdapter
import com.example.wetharpresnter.View.Home.HoursWeatherDataAdapter
import com.example.wetharpresnter.ViewModel.ViewModelFactory
import com.example.wetharpresnter.ViewModel.WeatherViewModel
import com.example.wetharpresnter.databinding.FragmentShowFavouritLocationsDataBinding
import java.util.*
import kotlin.collections.ArrayList


class ShowFavouriteLocationsData(var weatherData: WeatherData) : Fragment() {
    lateinit var binding: FragmentShowFavouritLocationsDataBinding
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModelProvider: WeatherViewModel
    lateinit var configrations: SharedPreferences
    lateinit var geoCoder: Geocoder
    var addressList = arrayListOf<Address>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowFavouritLocationsDataBinding.inflate(inflater, container, false)
        viewModelFactory = ViewModelFactory(requireContext())
        geoCoder = Geocoder(requireContext())
        viewModelProvider = ViewModelProvider(requireActivity(), viewModelFactory).get(
            WeatherViewModel::class.java
        )
        configrations = activity?.getSharedPreferences("Configuration", Context.MODE_PRIVATE)!!


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getWeatherData(weatherData)
    }

    fun getWeatherData(weatherData: WeatherData) {
        if (NetworkListener.getConnectivity(requireContext())) {
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
        }
        binding.swiperefresh.setOnRefreshListener {
            viewModelProvider.addToFav(
                weatherData.lat.toString(),
                weatherData.lon.toString()
            )
        }
    }

    fun _setWeatherData(weatherData: WeatherData) {
        var simpleDate = SimpleDateFormat("dd-M-yyyy")
        var currentDate = simpleDate.format(weatherData.current?.dt?.times(1000L) )
        var date: Date = simpleDate.parse(currentDate)
        println(date.toString())
        var time = date.toString().split(" ")
        binding.date.text="${time[0]} ${time[2]} ${time[1]} ${time[5]}"
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
            adapter = HoursWeatherDataAdapter(weatherData.hourly,configrations)
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


