package com.example.rancho

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.rancho.databinding.ActivitySplashBinding
import java.lang.StringBuilder

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)

        binding.txtName.animate().translationX(-1600F).setDuration(1000).setStartDelay(4030)

        binding.txtVersion.text = "Versão 2.0.1  - by ${StringBuilder("R1C4RD0").reversed()}"

        setContentView(binding.root)


        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)


    }
}