package com.example.wetharpresnter.View.Home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.wetharpresnter.ViewModel.HomeViewModel
import com.example.wetharpresnter.ViewModel.ViewModelFactory
import com.example.wetharpresnter.databinding.FragmentHomeBinding


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment(var viewPager: ViewPager2) : Fragment() {
    lateinit var binding: FragmentHomeBinding
    var observeOnce = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAndSetWeatherData()



    }

    override fun onResume() {
        super.onResume()
        getAndSetWeatherData()


    }

    fun getAndSetWeatherData(){

        var viewModelFactory = ViewModelFactory(requireContext())
        var viewModelProvider  =ViewModelProvider(requireActivity(),viewModelFactory).get(HomeViewModel::class.java)
        viewModelProvider.getLocation()
        viewModelProvider.accessList.observe(requireActivity()){ weatherData->

            binding.tvCityName.text = weatherData.timezone
            var temp =Math.ceil(weatherData.current?.temp ?: 0.0).toInt()

            binding.tvTempreture.text = temp.toString()+"°C"
            binding.tvWetharState.text = weatherData.current?.weather?.get(0)?.main

            var uri = "https://openweathermap.org/img/wn/${weatherData.current?.weather?.get(0)?.icon}@2x.png"
            Glide.with(requireActivity()).load(uri).into(binding.ivWetharState)
            binding.tvHumidity.text = Math.ceil((weatherData.current?.humidity)?.toDouble() ?: 0.0).toInt().toString()
            binding.tvPressure.text = Math.ceil((weatherData.current?.pressure)?.toDouble() ?: 0.0).toInt().toString()
            binding.tvWindSpeed.text = Math.ceil(weatherData.current?.wind_speed?:5.0).toInt().toString()

            binding.rvHoursWeather.apply {
                adapter = HoursWeatherDataAdapter(weatherData.hourly)
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            }

            binding.rvDayWeather.apply {
                adapter=DaysWeatherDataAdapter(weatherData.daily)
                layoutManager=LinearLayoutManager(requireContext())
            }


        }

        handelViewPagerWithRecycleView()
    }
    fun handelViewPagerWithRecycleView(){
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
        Log.i("Stoped", "onPause: ")
    }

}