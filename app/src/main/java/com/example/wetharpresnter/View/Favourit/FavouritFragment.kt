package com.example.wetharpresnter.View.Favourit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.wetharpresnter.R

/**
 * A simple [Fragment] subclass.
 * Use the [FavouritFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouritFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourit, container, false)
    }



}