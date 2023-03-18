package com.example.wetharpresnter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wetharpresnter.databinding.ActivityMainBinding

class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lottieSplash.animate().translationX(1400f).setStartDelay(5700)
            .withEndAction {
                startActivity(Intent(this,SettingScreen::class.java))
                finish()
        }
    }
}