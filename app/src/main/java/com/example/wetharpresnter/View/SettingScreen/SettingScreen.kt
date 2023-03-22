package com.example.wetharpresnter.View.SettingScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wetharpresnter.View.MainActivity.MainActivity
import com.example.wetharpresnter.databinding.ActivitySettingScreenBinding

class SettingScreen : AppCompatActivity() {
    lateinit var binding: ActivitySettingScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySettingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSaveSetting.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}