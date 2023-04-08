package com.example.wetharpresnter.View.Alert

import android.Manifest
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wetharpresnter.Constants
import com.example.wetharpresnter.NetworkListener
import com.example.wetharpresnter.R
import com.example.wetharpresnter.ViewModel.AlertViewModel.AlertViewModel
import com.example.wetharpresnter.ViewModel.AlertViewModel.AlertViewModelFactory
import com.example.wetharpresnter.databinding.FragmentAlertBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_12H
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [AlertFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlertFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentAlertBinding
    lateinit var dialog: Dialog
    lateinit var alertDialog: Dialog
    lateinit var map: MapView
    lateinit var btnSaveLocation: Button
    var lat: Double? = null
    var lon: Double? = null
    lateinit var snakbar: Snackbar
    lateinit var timePicker: MaterialTimePicker
    lateinit var calender: Calendar
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent
    var countryname = ""
    lateinit var viewModelFactory: AlertViewModelFactory
    lateinit var viewModelProvider: AlertViewModel
    var time = 1L
    var startMonth=""
    var startDay=""
    var endMonth=""
    var endDay=""
    var notificationId :Int= -1



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlertBinding.inflate(inflater, container, false)

        dialogInit(savedInstanceState)
        alertDialog = Dialog(requireContext())


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onViewCreated(view, savedInstanceState)
        /* var notification = LocationNotification(requireContext(),"titleeeeeeeee")
         notification.createNotificationChannel()
         */
        viewModelFactory = AlertViewModelFactory(requireContext())
        viewModelProvider =
            ViewModelProvider(requireActivity(), viewModelFactory).get(AlertViewModel::class.java)
        binding.rvAlert.apply {
            adapter = AlertAdapter(ArrayList(), viewModelProvider)
        }
        calender = Calendar.getInstance()
        locationSearch()



        binding.addLocation.setOnClickListener {
            showMap()
            // cancleAlarm()

        }

    }

    private fun cancleAlarm() {
        alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, AlarmRecever::class.java)
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(requireContext(), "Alarm set Sucssesfuly ", Toast.LENGTH_LONG).show()


    }

    private fun setAlarm() {
        var interval =(endDay.toInt()-startDay.toInt())+((endMonth.toInt()-startMonth.toInt())*30)
        var dayMiliSecond=24*60*1000

        for (i in 0..interval){
            alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
            val intent = Intent(activity, AlarmRecever::class.java)
            intent.putExtra("id",notificationId)
            println(notificationId)
            pendingIntent = PendingIntent.getBroadcast(
                context, notificationId+i //id
                , intent, 0
            )
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP, time+(i*dayMiliSecond), pendingIntent
            )
        }

        println(time)
        Toast.makeText(requireContext(), "Alarm set Sucssesfuly ", Toast.LENGTH_LONG).show()

    }

    private fun showTimePicker(): Pair<String, Long> {

        var datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfmonth ->
            calender.set(Calendar.YEAR, year)
            calender.set(Calendar.MONTH, month)
            calender.set(Calendar.DAY_OF_MONTH, dayOfmonth)
            updateDatelabel(calender)
            timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Ararm Time")
                .build()
            timePicker.show(childFragmentManager, Constants.CHANNEL_ID)

            timePicker.addOnPositiveButtonClickListener {
                calender[Calendar.HOUR_OF_DAY] = timePicker.hour
                calender[Calendar.MINUTE] = timePicker.minute
                calender[Calendar.SECOND] = 0
                calender[Calendar.MILLISECOND] = 0
                createNotificationChanel()
                time = calender.timeInMillis
                //alertDialog.show()

            }

        }
        DatePickerDialog(
            requireContext(),
            datePicker,
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        ).show()
        var simpleDate = android.icu.text.SimpleDateFormat("dd-M-yyyy").format(time)


        return Pair(simpleDate.toString(), time)
    }

    private fun updateDatelabel(calender: Calendar) {
        val day = SimpleDateFormat("dd").format(calender.time)
        val month = SimpleDateFormat("MM").format(calender.time)
        val year = SimpleDateFormat("yyyy").format(calender.time)

        println("$day : $month : $year")

    }

    private fun createNotificationChanel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            notificationId=generateUniqueIntValue(lat?.toLong() ?: 1L, lon?.toLong() ?: 5L,time.toString(),endMonth)
            println(notificationId.toString()+ "  from chanel ")
            val channel = NotificationChannel(notificationId.toString(), name, importance).apply {
                this.enableLights(true)
                this.enableVibration(true)
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            with(NotificationManagerCompat.from(requireContext())) {
                // notificationId is a unique int for each notification that you must define
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return
                }
            }

        }
    }

    private fun showMap() {
        if (NetworkListener.getConnectivity(requireContext())) {
            dialog.show()
        } else {
            snakbar = Snackbar.make(
                binding.rvAlert,
                "No Network Connection",
                Snackbar.LENGTH_LONG
            )
            snakbar.show()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        var selectedLocation = LatLng(63.0, 63.0)
        var markerOption = MarkerOptions().position(selectedLocation).title("selected")
        var marker = googleMap.addMarker(markerOption)
        googleMap.setOnMapLongClickListener { lis ->

            var selectedLocation = LatLng(lis.latitude, lis.longitude)
            marker?.position = selectedLocation
            lat = lis.latitude
            lon = lis.longitude

            btnSaveLocation.setOnClickListener {
                dialog.dismiss()
                //   showTimePicker()
                countryname =
                    getString(R.string.aler_cofirmation) + "\n" + countryName(lat!!, lon!!)
                alertDialogInit()
                alertDialog.show()
                // viewModelProvider.addToFav(lat.toString(), lon.toString())


            }
        }
    }

    fun dialogInit(savedInstanceState: Bundle?) {
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.map_picker)

        map = dialog.findViewById(R.id.mv_fav_locations)
        val window: Window? = dialog.getWindow()
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.MATCH_PARENT
        )
        map.getMapAsync(this)
        map.onCreate(savedInstanceState)
        btnSaveLocation = dialog.findViewById(R.id.btn_save_location)

        btnSaveLocation.setOnClickListener {
            dialog.dismiss()

        }
    }

    fun alertDialogInit() {
        var startDate = Pair("", 0L)
        var endDate = Pair("", 0L)



        alertDialog.setContentView(R.layout.alert_dialog)

        val window: Window? = alertDialog.getWindow()
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.findViewById<TextView>(R.id.tv_country).text = countryname
        alertDialog.findViewById<ImageView>(R.id.iv_from_date).setOnClickListener {
            startDate = showTimePicker()
            startMonth = startDate.first.split("-").get(1)
            startDay= startDate.first.split("-").get(0)

        }
        alertDialog.findViewById<ImageView>(R.id.iv_to_date).setOnClickListener {
            endDate = showTimePicker()
            endMonth = endDate.first.split("-").get(1)
            endDay= endDate.first.split("-").get(0)
        }

        alertDialog.findViewById<Button>(R.id.btn_save_alert).setOnClickListener {
            println(generateUniqueKey().toString()+"d5aaaaaaaaaaaaaaaaaaaaaaaaaal")

            if (alertDialog.findViewById<RadioButton>(R.id.rb_alert_notification).isChecked) {
                viewModelProvider.addToAlert(
                    lat.toString(),
                    lon.toString(),
                   notificationId,
                    startDate.second,
                    startDate.first,
                    endDate.second,
                    endDate.first,
                    Constants.NOTIFICATIONS
                )

            } else {
                if (!Settings.canDrawOverlays(context)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + context?.getPackageName())
                    )
                    requireActivity().startActivityForResult(intent, 0)
                }else{
                    viewModelProvider.addToAlert(
                        lat.toString(),
                        lon.toString(),
                       notificationId,
                        startDate.second,
                        startDate.first,
                        endDate.second,
                        endDate.first,
                        Constants.ALARM
                    )

                }


            }
            setAlarm()

            alertDialog.dismiss()

        }
        alertDialog.findViewById<Button>(R.id.btn_cancel_alert).setOnClickListener {
            alertDialog.dismiss()

        }


    }

    fun generateUniqueKey(): Int {
        var counter = AtomicInteger(0)
        return counter.getAndIncrement()
    }

    fun generateUniqueIntValue(a: Long, b: Long, str: String, strType: String): Int {
        val input = "$a$b$str$strType"
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray(StandardCharsets.UTF_8))
        val truncatedHash = hash.copyOfRange(0, 4) // Truncate hash to 4 bytes
        return truncatedHash.fold(0) { acc, byte -> (acc shl 8) + (byte.toInt() and 0xff) }
    }

    override fun onResume() {
        super.onResume()
        // viewModelProvider.getFavLocations()
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

    fun locationSearch() {
        dialog.findViewById<SearchView>(R.id.sv_location_search).setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                var geoCoder = Geocoder(requireContext())
                var addressList = arrayListOf<Address>()
                addressList = geoCoder.getFromLocationName(query, 1) as ArrayList<Address>
                if (addressList.size > 0) {
                    var address = addressList.get(0)
                    var lat = address.latitude
                    var long = address.longitude
                    goToAddress(lat, long, 10f)

                }
                return false
            }

        })


    }

    private fun goToAddress(lat: Double, lon: Double, fl: Float) {
        var latLang = LatLng(lat, lon)
        var camera = CameraUpdateFactory.newLatLngZoom(latLang, fl)
        map.getMapAsync { it.animateCamera(camera) }
    }

    private fun countryName(lat: Double, lon: Double): String {
        var address = ""
        var geoCoder: Geocoder = Geocoder(requireContext())
        var addressList = arrayListOf<Address>()
        addressList = geoCoder.getFromLocation(lat, lon, 1) as ArrayList<Address>
        if (addressList.size > 0) {
            address = addressList.get(0).countryName
        }
        return address
    }

}