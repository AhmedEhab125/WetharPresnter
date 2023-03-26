package com.example.wetharpresnter.View.Home

import android.R
import android.app.Dialog
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
        dialogSettingView()

        binding.swiperefresh.setOnRefreshListener {
            getAndSetWeatherData()
        }
        binding.shimmerViewContainer.startShimmer() // If auto-start is set to false


    }

    override fun onResume() {
        super.onResume()


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

            binding.shimmerViewContainer.hideShimmer()
            binding.swiperefresh.isRefreshing=false
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
    fun dialogSettingView(){
        var dialog =Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(com.example.wetharpresnter.R.layout.start_setting_dialog_iteam)
        val window: Window? = dialog.getWindow()
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawableResource(R.color.transparent);
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        dialog.findViewById<Button>(com.example.wetharpresnter.R.id.btn_save).setOnClickListener {
            dialog.dismiss()
            getAndSetWeatherData()
        }

    }

}