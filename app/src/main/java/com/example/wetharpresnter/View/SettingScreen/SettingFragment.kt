package com.example.wetharpresnter.View.SettingScreen

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.wetharpresnter.Utils.Constants
import com.example.wetharpresnter.Netwoek.NetworkListener
import com.example.wetharpresnter.View.MainActivity.MainActivity
import com.example.wetharpresnter.databinding.FragmentSettingBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*


class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters

    lateinit var binding: FragmentSettingBinding
    lateinit var configrations: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        configrations = requireActivity().getSharedPreferences(
            "Configuration",
            AppCompatActivity.MODE_PRIVATE
        )!!


        chosenSetting()
        selectlang()
        selectLocation()
        selectUnit()
    if (!NetworkListener.getConnectivity(requireContext())){
        binding.rbCelsius.isClickable=false
        binding.rbKelvin.isClickable=false
        binding.rbFahrenheit.isClickable=false
        Snackbar.make(binding.scrol,"No Network",Snackbar.LENGTH_LONG).show()
    }

        return binding.root


    }

    fun chosenSetting() {
        // Units check
        if (configrations.getString(Constants.UNITS, "").equals(Constants.METRIC)) {
            binding.rbCelsius.isChecked = true
        } else if (configrations.getString(Constants.UNITS, "").equals(Constants.IMPERIAL)) {
            binding.rbFahrenheit.isChecked = true
        } else if (configrations.getString(Constants.UNITS, "").equals(Constants.DEFAULT)) {
            binding.rbKelvin.isChecked = true
        }

        //lang
        if (configrations.getString(Constants.LANG, "").equals(Constants.ENGLISH)) {
            binding.rbEnglish.isChecked = true
        } else if (configrations.getString(Constants.LANG, "").equals(Constants.ARABIC)) {
            binding.rbArabic.isChecked = true
        }
        //location
        if (configrations.getString(Constants.LOCATION,"").equals(Constants.GPS)){
            binding.rbGPS.isChecked=true
        }else if (configrations.getString(Constants.LOCATION,"").equals(Constants.MAP)){
            binding.rbChosseLocationFromMap.isChecked=true
        }

    }
    fun selectlang(){
        binding.rbArabic.setOnClickListener {
            configrations.edit()?.putString(Constants.LANG, Constants.ARABIC)?.apply()
            setLocale(Constants.ARABIC)
        }
        binding.rbEnglish.setOnClickListener {
            configrations.edit()?.putString(Constants.LANG, Constants.ENGLISH)?.apply()
            setLocale(Constants.ENGLISH)
        }
    }
    fun selectLocation(){
        binding.rbGPS.setOnClickListener {
            configrations.edit()?.putString(Constants.LOCATION, Constants.GPS)?.apply()
        }
        binding.rbChosseLocationFromMap.setOnClickListener {
            configrations.edit()?.putString(Constants.LOCATION, Constants.MAP)?.apply()
            Constants.mapFlag=true
        }

    }
    fun selectUnit(){
        binding.rbCelsius.setOnClickListener {
            configrations.edit()?.putString(Constants.UNITS, Constants.METRIC)?.apply()
        }
        binding.rbFahrenheit.setOnClickListener {
            configrations.edit()?.putString(Constants.UNITS, Constants.IMPERIAL)?.apply()
        }
        binding.rbKelvin.setOnClickListener {
            configrations.edit()?.putString(Constants.UNITS, Constants.DEFAULT)?.apply()
        }

    }
    private fun setLocale(lang: String?) {
        var locale = Locale(lang)
        Locale.setDefault(locale)
        var config = Configuration()
        config.setLocale(locale)
        context?.resources?.updateConfiguration(config,context?.resources?.displayMetrics)
        /*
        parentFragmentManager.beginTransaction().detach(SettingFragment@this).commitNowAllowingStateLoss()
        parentFragmentManager.beginTransaction().attach(SettingFragment@this).commitNowAllowingStateLoss()

        parentFragmentManager.beginTransaction().detach(HomeFragment@this).commitNowAllowingStateLoss()
        parentFragmentManager.beginTransaction().attach(HomeFragment@this).commitNowAllowingStateLoss()
    */
        activity?.startActivity(Intent(context,MainActivity::class.java))
        activity?.finish()
       // activity?.recreate()


        }

    override fun onResume() {
        super.onResume()
        if (!NetworkListener.getConnectivity(requireContext())){
            binding.rbCelsius.isClickable=false
            binding.rbKelvin.isClickable=false
            binding.rbFahrenheit.isClickable=false
            Snackbar.make(binding.constran,"No Network",Snackbar.LENGTH_LONG).show()
        }else{
            binding.rbCelsius.isClickable=true
            binding.rbKelvin.isClickable=true
            binding.rbFahrenheit.isClickable=true
        }
    }
}