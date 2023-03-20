package com.example.wetharpresnter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.wetharpresnter.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var vpFragmentAdapter: VPFragmentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val iconList= listOf(R.drawable.home,R.drawable.heart,R.drawable.bell,R.drawable.settings)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vpFragmentAdapter= VPFragmentAdapter(listOf(HomeFragment(),FavouritFragment(),AlertFragment(),SettingFragment()),supportFragmentManager,this.lifecycle)
        binding.vpScreenTitles.adapter=vpFragmentAdapter
        TabLayoutMediator(binding.TabLayoutScreens,binding.vpScreenTitles,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->tab .icon=getDrawable(iconList.get(position)) }).attach()

    }
}