package com.example.wetharpresnter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    lateinit var weatherData: WeatherData
    lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container, false)
        // Inflate the layout for this fragment
        return binding.root




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {

            launch {
                weatherData= Repository.getWetharData("33.44","-94.04")?: WeatherData()

            }.join()
            launch(Dispatchers.Main) { binding.tvCityName.text=weatherData.sys?.country
            binding.tvTempreture.text=weatherData.main?.temp.toString()
                binding.tvWetharState.text=weatherData.weather.get(0).main

                var uri ="https://openweathermap.org/img/wn/${weatherData.weather.get(0).icon}@2x.png"

                Glide.with(requireActivity()).load(uri).into(binding.ivWetharState)
            }

        }
    }

}