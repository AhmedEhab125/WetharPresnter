package com.example.wetharpresnter.View.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.wetharpresnter.MainActivity
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Repository
import com.example.wetharpresnter.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment(var viewPager: ViewPager2) : Fragment() {
    lateinit var weatherData: WeatherData
    lateinit var binding: FragmentHomeBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {

            launch {
                weatherData= Repository.getWetharData("33.44", "-94.04") ?: WeatherData()

            }.join()
            launch(Dispatchers.Main) { binding.tvCityName.text=weatherData.timezone
            binding.tvTempreture.text=weatherData.current?.temp.toString()
                binding.tvWetharState.text= weatherData.current?.weather?.get(0)?.main

                var uri ="https://openweathermap.org/img/wn/${weatherData.current?.weather?.get(0)?.icon}@2x.png"
                Glide.with(requireActivity()).load(uri).into(binding.ivWetharState)
                binding.tvHumidity.text=weatherData.current?.humidity.toString()
                binding.tvPressure.text=weatherData.current?.pressure.toString()
                binding.tvWindSpeed.text=weatherData.current?.windSpeed.toString()
                binding.rvHoursWeather.apply {
                    adapter= HoursWeatherDataAdapter(weatherData.hourly)
                    layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

                }

                binding.rvHoursWeather.addOnItemTouchListener(object : OnItemTouchListener {
                    var lastX = 0
                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        when (e.action) {
                            MotionEvent.ACTION_DOWN -> lastX = e.x.toInt()
                            MotionEvent.ACTION_MOVE -> {
                                val isScrollingRight = e.x < lastX
                                if (isScrollingRight && (binding.rvHoursWeather.getLayoutManager() as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == (binding.rvHoursWeather.getAdapter()
                                    ?.getItemCount() ?: 0) - 1 || !isScrollingRight && (binding.rvHoursWeather.getLayoutManager() as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0
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

        }
    }

}