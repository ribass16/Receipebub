package com.example.pap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.pap.databinding.ActivityMainBinding
import com.example.pap.databinding.ActivitySplashscreenBinding

class SplashScreen : AppCompatActivity() {

    private val binding by lazy {
        ActivitySplashscreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }, 2500)

    }
}