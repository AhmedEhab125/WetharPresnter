package com.example.wetharpresnter.View.SettingScreen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.wetharpresnter.Constants
import com.example.wetharpresnter.R
import com.example.wetharpresnter.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters

    lateinit var binding: FragmentSettingBinding
    lateinit var configrations: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSettingBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        configrations = requireActivity().getSharedPreferences("Configuration", AppCompatActivity.MODE_PRIVATE)!!
        configrations.edit()?.putString(Constants.LANG, Constants.ENGLISH)?.apply()
        configrations.getString(Constants.LOCATION, "").equals(Constants.MAP)



        return binding.root


    }


}