package com.example.wetharpresnter.View.Favourit

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import com.example.wetharpresnter.R
import com.example.wetharpresnter.databinding.FragmentFavouritBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * A simple [Fragment] subclass.
 * Use the [FavouritFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouritFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentFavouritBinding
    lateinit var dialog: Dialog
    lateinit var map: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavouritBinding.inflate(layoutInflater, container, false)
        dialogInit(savedInstanceState)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.addLocation.setOnClickListener {
            showDeleteMealDialog()
        }
    }

    private fun showDeleteMealDialog() {


        dialog.show()


    }

    override fun onMapReady(googleMap: GoogleMap) {
        val sydney = LatLng(30.9, 60.99)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
    fun dialogInit(savedInstanceState: Bundle?){
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.map_picker)
        map = dialog.findViewById(R.id.mv_fav_locations)
        val window: Window? = dialog.getWindow()
        window?.setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.MATCH_PARENT)
        map.getMapAsync(this)
        map.onCreate(savedInstanceState)
        dialog.findViewById<Button>(R.id.btn_save_location).setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        map.onStop()
    }

    override fun onStart() {
        super.onStart()
        map.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }
}
