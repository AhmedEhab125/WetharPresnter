package com.example.wetharpresnter.View.SplashScreen

import android.R
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.RadioGroup
import androidx.constraintlayout.widget.Constraints
import com.example.wetharpresnter.Constants
import com.example.wetharpresnter.View.MainActivity.MainActivity
import com.example.wetharpresnter.View.SettingScreen.SettingScreen
import com.example.wetharpresnter.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    lateinit var configrations: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configrations = getSharedPreferences("Configuration", MODE_PRIVATE)!!
        binding.lottieSplash.animate().translationX(1400f).setStartDelay(5700)
            .withEndAction {
                dialogSettingView()

            }
    }

    fun dialogSettingView() {
        var dialog = Dialog(this)
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
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            var location =
                dialog.findViewById<RadioGroup>(com.example.wetharpresnter.R.id.rg_location)
            onRadioButtonClickedLocation(location)
            var lang = dialog.findViewById<RadioGroup>(com.example.wetharpresnter.R.id.rg_lang)
            onRadioButtonClickedLang(lang)
            dialog.dismiss()

        }
    }

    fun onRadioButtonClickedLang(radioGroup: RadioGroup) {
        var selected = radioGroup.checkedRadioButtonId

        if (selected == com.example.wetharpresnter.R.id.rb_english) {
            configrations.edit()?.putString(Constants.LANG, Constants.ENGLISH)?.apply()

        } else if (selected == com.example.wetharpresnter.R.id.rb_arabic) {
            configrations.edit()?.putString(Constants.LANG, Constants.ARABIC)?.apply()
        }

    }
    fun onRadioButtonClickedLocation(radioGroup: RadioGroup) {
        var selected = radioGroup.checkedRadioButtonId
        if (selected == com.example.wetharpresnter.R.id.rb_GPS) {
            configrations.edit()?.putString(Constants.LOCATION, Constants.GPS)?.apply()
        } else if (selected == com.example.wetharpresnter.R.id.rb_chosse_location_from_map) {
            configrations.edit()?.putString(Constants.LOCATION, Constants.MAP)?.apply()
        }


    }



}