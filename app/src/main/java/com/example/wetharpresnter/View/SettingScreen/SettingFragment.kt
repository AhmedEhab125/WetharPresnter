package com.example.wetharpresnter.View.SettingScreen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wetharpresnter.Constants
import com.example.wetharpresnter.R
import com.example.wetharpresnter.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters

    lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSettingBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val status = sharedPref.getString(Constants.LANG,"")
        val units = sharedPref.getString("units","metric")
        val language = sharedPref.getString("language","en")
        val editor = sharedPref.edit()

    }


}