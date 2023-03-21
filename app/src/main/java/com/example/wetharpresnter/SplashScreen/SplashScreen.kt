package com.example.wetharpresnter.SplashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wetharpresnter.MainActivity
import com.example.wetharpresnter.SettingScreen.SettingScreen
import com.example.wetharpresnter.databinding.ActivityMainBinding
import com.example.wetharpresnter.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lottieSplash.animate().translationX(1400f).setStartDelay(5700)
            .withEndAction {
                startActivity(Intent(this, SettingScreen::class.java))
                finish()
        }
    }
}