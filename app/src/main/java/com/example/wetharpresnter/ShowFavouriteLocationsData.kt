package com.example.wetharpresnter

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.View.Home.DaysWeatherDataAdapter
import com.example.wetharpresnter.View.Home.HoursWeatherDataAdapter
import com.example.wetharpresnter.ViewModel.ViewModelFactory
import com.example.wetharpresnter.ViewModel.WeatherViewModel
import com.example.wetharpresnter.databinding.FragmentShowFavouritLocationsDataBinding


class ShowFavouriteLocationsData(var weatherData: WeatherData) : Fragment() {
    lateinit var binding: FragmentShowFavouritLocationsDataBinding
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModelProvider: WeatherViewModel
    lateinit var configrations: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowFavouritLocationsDataBinding.inflate(inflater, container, false)
        viewModelFactory = ViewModelFactory(requireContext())
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
        binding.swiperefresh.isRefreshing = false
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
            adapter = DaysWeatherDataAdapter(weatherData.daily, configrations)
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.shimmerViewContainer.hideShimmer()

    }


}


